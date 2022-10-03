package com.sangboyoon.accounter.web.bookmark.dto;

import com.sangboyoon.accounter.domain.bookmark.Bookmark;
import com.sangboyoon.accounter.domain.corporation.Corporation;
import lombok.Getter;

@Getter
public class GetBookmarkResponse {
    private final Long bookmarkId;
    private final Corporation corpCode;

    public GetBookmarkResponse(Bookmark bookmark) {
        bookmarkId = bookmark.getId();
        corpCode = bookmark.getCorporation();
    }

}
