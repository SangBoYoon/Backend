package com.sangboyoon.accounter.web.users.sign.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteUserRequest {
    String accessToken;
    String refreshToken;
}
