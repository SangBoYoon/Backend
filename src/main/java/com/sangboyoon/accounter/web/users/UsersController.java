package com.sangboyoon.accounter.web.users;

import com.sangboyoon.accounter.application.user.UserUseCase;
import com.sangboyoon.accounter.configuration.security.CurrentUser;
import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.web.common.ApiResponse;
import com.sangboyoon.accounter.web.users.dto.UpdateUserRequest;
import com.sangboyoon.accounter.web.users.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/accounter")
@RequiredArgsConstructor
public class UsersController {
    private final UserUseCase userUseCase;

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UserDto> userInfo(@CurrentUser User user) {
        return new ApiResponse<>(userUseCase.findUser(user.getId()));
    }

    @PatchMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UserDto> updateUser(@CurrentUser User user, @RequestBody @Valid UpdateUserRequest request) {
        return new ApiResponse<>(userUseCase.update(user.getId(), request));
    }

}
