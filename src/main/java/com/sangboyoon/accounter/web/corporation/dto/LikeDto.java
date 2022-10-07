package com.sangboyoon.accounter.web.corporation.dto;

import com.sangboyoon.accounter.domain.corporation.Corporation;
import com.sangboyoon.accounter.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LikeDto {
    private User user;
    private Corporation corporation;
}
