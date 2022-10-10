package com.sangboyoon.accounter.configuration.security.OAuth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sangboyoon.accounter.configuration.security.JwtTokenProvider;
import com.sangboyoon.accounter.domain.security.exceptions.CEmailLoginFailedException;
import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.domain.user.UserRepository;
import com.sangboyoon.accounter.web.users.sign.dto.SignupUserRequest;
import com.sangboyoon.accounter.web.users.sign.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider tokenProvider;
    private final UserRequestMapper userRequestMapper;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        SignupUserRequest userDto = userRequestMapper.getUser(oAuth2User);

        log.info("Principal 에서 꺼낸 OAuth2User = {}", oAuth2User);
        log.info("userDto.email = {}, userDto.nickName = {}", userDto.getEmail(), userDto.getNickName());
        String targetUrl;
        log.info("토큰 발행 시작");

        if(!userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            userRepository.save(SignupUserRequest.toEntity(userDto.getEmail(), userDto.getNickName(), userDto.getRole()));
        }

        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(CEmailLoginFailedException::new);

        TokenDto tokenDto = tokenProvider.createTokenDto(user.getId(), "USER");
        log.info("{}", tokenDto.toString());
        targetUrl = UriComponentsBuilder.fromUriString("http://laccounter-s3.s3-website.ap-northeast-2.amazonaws.com/login/token")
                .queryParam("accessToken", tokenDto.getAccessToken())
                .queryParam("refreshToken", tokenDto.getRefreshToken())
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
