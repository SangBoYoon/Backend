package com.sangboyoon.accounter.application.user;

import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.web.users.dto.UpdateUserRequest;
import com.sangboyoon.accounter.web.users.dto.UserDto;
import com.sangboyoon.accounter.web.users.sign.dto.DeleteUserRequest;

public interface UserUseCase {
    UserDto findUser(Long id);
    UserDto update(Long id, UpdateUserRequest request);
    String deleteUser(DeleteUserRequest request);
}
