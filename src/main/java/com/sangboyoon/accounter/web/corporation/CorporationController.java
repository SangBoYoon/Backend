package com.sangboyoon.accounter.web.corporation;

import com.sangboyoon.accounter.application.corporation.CorporationUseCase;
import com.sangboyoon.accounter.configuration.security.CurrentUser;
import com.sangboyoon.accounter.domain.corporation.Corporation;
import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.web.common.ApiResponse;
import com.sangboyoon.accounter.web.corporation.dto.CorporationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/accounter")
public class CorporationController {
    private final CorporationUseCase corporationUseCase;

    @GetMapping("/corps")
    public ApiResponse<List<CorporationDto>> findAllCorporation() {
        List<Corporation> corporations = corporationUseCase.findAllCorporation();
        return new ApiResponse<>(corporations.stream()
                .map(CorporationDto::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/corp/{corpCode}")
    public ApiResponse<CorporationDto> findCorporation(@PathVariable("corpCode") String corpCode) {
        CorporationDto corporations = corporationUseCase.findCorporation(corpCode);
        return new ApiResponse<>(corporations);
    }

    @PostMapping("/corp/like/{corpCode}")
    public ApiResponse<Integer> likeCorporation(@CurrentUser User user, @PathVariable("corpCode") String corpCode) {
        int result = corporationUseCase.saveLike(corpCode, user);
        return new ApiResponse<>(result);
    }

    @GetMapping("/corpCreate")
    public ApiResponse<String> startQuery() {
        corporationUseCase.startQuery();
        return new ApiResponse<>("create success");
    }
}
