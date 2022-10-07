package com.sangboyoon.accounter.application.corporation;

import com.sangboyoon.accounter.domain.corporation.Corporation;
import com.sangboyoon.accounter.domain.corporation.LikeEntity;
import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.web.corporation.dto.CorporationDto;
import com.sangboyoon.accounter.web.corporation.dto.LikeResponseDto;

import java.util.List;

public interface CorporationUseCase {
    List<Corporation> findAllCorporation();
    CorporationDto findCorporation(String corpCode);
    int saveLike(String corpCode, User user);
    List<LikeResponseDto> findLikeByUserId(User user);
    void startQuery();
    List<List<String>> readCSV();
}
