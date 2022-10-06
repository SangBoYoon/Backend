package com.sangboyoon.accounter.configuration.security.OAuth2;

import com.sangboyoon.accounter.web.users.sign.dto.SignupUserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {

    public SignupUserRequest getUser(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return SignupUserRequest.builder()
                .nickName((String) attributes.get("nickName"))
                .email((String) attributes.get("email"))
                .password("")
                .role("SNS_USER")
                .build();
    }
}
