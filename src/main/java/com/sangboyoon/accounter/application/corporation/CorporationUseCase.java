package com.sangboyoon.accounter.application.corporation;

import com.sangboyoon.accounter.domain.corporation.Corporation;
import com.sangboyoon.accounter.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface CorporationUseCase {
    List<Corporation> findAllCorporation();
    Optional<Corporation> findCorporation(String corpCode);
    int saveLike(String corpCode, User user);
    void startQuery();
    List<List<String>> readCSV();
}
