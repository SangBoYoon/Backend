package com.sangboyoon.accounter.web.users.dto;

import com.sangboyoon.accounter.domain.user.User;
import lombok.Getter;

@Getter
public class UserDto {
    private final Long id;
    private final String email;
    private final String nickName;
    private final String role;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickName = user.getNickName();
        this.role = user.getRole();
    }
}
