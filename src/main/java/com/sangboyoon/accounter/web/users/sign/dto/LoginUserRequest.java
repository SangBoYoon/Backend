package com.sangboyoon.accounter.web.users.sign.dto;

import com.sangboyoon.accounter.advice.ValidationGroups;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserRequest {
    @NotBlank(message = "이메일은 필수 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Email(regexp = "[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}",
        message = "이메일 형식이 잘못되었습니다.", groups = ValidationGroups.PatternCheckGroup.class)
    private String email;

    @NotBlank(message = "비밀번호는 필수 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Length(min = 8, message = "비밀번호는 최소 {min}자 이상입니다.", groups = ValidationGroups.PatternCheckGroup.class)
    private String password;
}
