package com.sangboyoon.accounter.web.bookmark.dto;

import com.sangboyoon.accounter.domain.corporation.Corporation;
import lombok.Getter;

@Getter
public class GetBookmarkResponse {
    private final Corporation corpCode;

    public GetBookmarkResponse(AddBookmarkDto bookmark) {
        corpCode = bookmark.getCorporation();
    }

}
