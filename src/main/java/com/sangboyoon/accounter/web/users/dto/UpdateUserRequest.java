package com.sangboyoon.accounter.web.users.dto;

import com.sangboyoon.accounter.advice.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    @NotBlank(message = "비밀번호는 필수입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Length(min = 8, message = "비밀번호는 최소 (min)자 이상입니다.", groups = ValidationGroups.PatternCheckGroup.class)
    private String password;
    @NotBlank(message = "닉네임은 필수입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Length(min = 2, message = "닉네임은 최소 (min)자 이상입니다.", groups = ValidationGroups.PatternCheckGroup.class)
    private String nickName;
}
