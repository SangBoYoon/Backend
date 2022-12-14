package com.sangboyoon.accounter.web.users.sign.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class TokenRequest {
    @NotBlank(message = "access token 은 필수값입니다.")
    String accessToken;
    @NotBlank(message = "refresh token 은 필수값입니다.")
    String refreshToken;

    @Builder
    public TokenRequest(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}