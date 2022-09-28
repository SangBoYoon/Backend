package com.sangboyoon.accounter.application.user;

import com.sangboyoon.accounter.web.users.dto.UpdateUserRequest;
import com.sangboyoon.accounter.web.users.dto.UserDto;

public interface UserUseCase {
    UserDto findUser(Long id);
    UserDto update(Long id, UpdateUserRequest request);
}
