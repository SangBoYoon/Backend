package com.sangboyoon.accounter.application.corporation;

import com.sangboyoon.accounter.domain.corporation.Corporation;
import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.web.corporation.dto.CorporationDto;

import java.util.List;
import java.util.Optional;

public interface CorporationUseCase {
    List<Corporation> findAllCorporation();
    CorporationDto findCorporation(String corpCode);
    int saveLike(String corpCode, User user);
    void startQuery();
    List<List<String>> readCSV();
}
