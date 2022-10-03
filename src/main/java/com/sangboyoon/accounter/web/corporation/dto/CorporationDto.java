package com.sangboyoon.accounter.web.corporation.dto;

import com.sangboyoon.accounter.domain.corporation.Corporation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CorporationDto {
    private final String corpCode;
    private final Long likeCount;

    public CorporationDto(Corporation corporation) {
        this.corpCode = corporation.getCorpCode();
        this.likeCount = corporation.getCorpLike();
    }

}
