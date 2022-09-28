package com.sangboyoon.accounter.application.security;

import com.sangboyoon.accounter.configuration.security.JwtTokenProvider;
import com.sangboyoon.accounter.domain.security.exceptions.*;
import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.domain.user.UserRepository;
import com.sangboyoon.accounter.domain.user.exceptions.CUserNotFoundException;
import com.sangboyoon.accounter.web.users.sign.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@ResponseStatus
@Transactional
public class SignService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public SignService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, StringRedisTemplate stringRedisTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = stringRedisTemplate;
    }

    @Transactional
    public TokenDto login(LoginUserRequest loginUserRequest) {
        User user = userRepository.findByEmail(loginUserRequest.getEmail()).orElseThrow(CEmailLoginFailedException::new);

        if(!passwordEncoder.matches(loginUserRequest.getPassword(), user.getPassword())) throw new CPasswordLoginFailedException();

        TokenDto tokenDto = jwtTokenProvider.createTokenDto(user.getId(), user.getRole());

        redisTemplate.opsForValue()
                .set("RT:" + user.getId(),
                        tokenDto.getRefreshToken(), jwtTokenProvider.getRefreshTokenValidMillisecond(), TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    @Transactional
    public SignupUserResponse signup(SignupUserRequest userSignupDto) {
        if(userRepository.findByEmail(userSignupDto.getEmail()).isPresent()) throw new CEmailSignupFailedException();
        return new SignupUserResponse(userRepository.save(userSignupDto.toEntity(passwordEncoder)).getId());
    }

    @Transactional
    public TokenDto reissue(TokenRequest tokenRequest) {
        if(!jwtTokenProvider.validationToken(tokenRequest.getRefreshToken()))
            throw new CRefreshTokenException();

        String accessToken = tokenRequest.getAccessToken();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(CUserNotFoundException::new);
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + user.getId());

        if (!refreshToken.equals(tokenRequest.getRefreshToken()))
            throw new CRefreshTokenException();

        TokenDto newCreatedToken = jwtTokenProvider.createTokenDto(user.getId(), user.getRole());

        redisTemplate.opsForValue()
                .set("RT:" + user.getId(),
                        newCreatedToken.getRefreshToken(), jwtTokenProvider.getRefreshTokenValidMillisecond(), TimeUnit.MILLISECONDS);

        return newCreatedToken;
    }

    @Transactional
    public void logout(TokenRequest tokenRequest) {
        String accessToken = tokenRequest.getAccessToken();

        if (!jwtTokenProvider.validationToken(accessToken)) throw new CAccessDeniedException();

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(CUserNotFoundException::new);

        if (redisTemplate.opsForValue().get("RT:" + user.getId()) != null) {
            redisTemplate.delete("RT:" + user.getId());
        }

        Long expiration = jwtTokenProvider.getExpiration(tokenRequest.getAccessToken());
        redisTemplate.opsForValue()
                .set(tokenRequest.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
    }
}
