package com.sangboyoon.accounter.web.corporation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LikeResponseDto {
    private Long userId;
    private String corpCode;
}