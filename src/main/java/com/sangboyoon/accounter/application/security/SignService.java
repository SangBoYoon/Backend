package com.sangboyoon.accounter.application.security;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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

    @Transactional
    public String getKakaoAccessToken(String code) {
        String access_token = "";
        String refresh_token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            sb.append("grant_type=authorization_code");
            sb.append("&client_id=ead8e7984fdc096a6e450e0a638a8c8d"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:8080/api/kakao"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            sb.append("&client_secret=2lTkLrlaOjZWAGJ0PkhNVzWEBGk9HI3E");
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonObject element = JsonParser.parseString(result).getAsJsonObject();

            access_token = element.get("access_token").getAsString();
            refresh_token = element.get("refresh_token").getAsString();

            System.out.println("access_token : " + access_token);
            System.out.println("refresh_token : " + refresh_token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_token;
    }

    @Transactional
    public TokenDto createKakaoUser(String token) {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        SignupUserRequest request = new SignupUserRequest();
        TokenDto tokenDto = new TokenDto();

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonObject element = JsonParser.parseString(result).getAsJsonObject();

            String nickName = element.get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("nickname").getAsString();
            boolean hasEmail = element.get("kakao_account").getAsJsonObject().get("is_email_valid").getAsBoolean();
            String email = "";
            if(hasEmail){
                email = element.get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            System.out.println("nickname : " + nickName);
            System.out.println("email : " + email);

            br.close();

            userRepository.save(SignupUserRequest.toEntity(email, nickName));

            User user = userRepository.findByEmail(email).orElseThrow(CEmailLoginFailedException::new);

            tokenDto = jwtTokenProvider.createTokenDto(user.getId(), "USER");

            redisTemplate.opsForValue()
                    .set("RT:" + user.getId(),
                            tokenDto.getRefreshToken(), jwtTokenProvider.getRefreshTokenValidMillisecond(), TimeUnit.MILLISECONDS);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return tokenDto;
    }
}