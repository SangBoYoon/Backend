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
    private final String corpName;
    private final String corpCategory;
    private final Long likeCount;

    public CorporationDto(Corporation corporation) {
        this.corpCode = corporation.getCorpCode();
        this.corpName = corporation.getCorpName();
        this.corpCategory = corporation.getCorpCategory();
        this.likeCount = corporation.getCorpLike();
    }

}
