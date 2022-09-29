package com.sangboyoon.accounter.web.users.sign;

import com.sangboyoon.accounter.advice.ValidationSequence;
import com.sangboyoon.accounter.application.security.SignService;
import com.sangboyoon.accounter.web.common.ApiResponse;
import com.sangboyoon.accounter.web.users.sign.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TokenDto> login(@RequestBody @Validated(ValidationSequence.class)LoginUserRequest request) {
        TokenDto tokenDto = signService.login(request);
        return new ApiResponse<>(tokenDto);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SignupUserResponse> signup(@RequestBody @Validated(ValidationSequence.class)SignupUserRequest request) {
        return new ApiResponse<>(signService.signup(request));
    }

    @PostMapping("/reuissue")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TokenDto> reissue(@RequestBody @Valid TokenRequest request) {
        return new ApiResponse<>(signService.reissue(request));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody @Valid TokenRequest request) { signService.logout(request); }
}
