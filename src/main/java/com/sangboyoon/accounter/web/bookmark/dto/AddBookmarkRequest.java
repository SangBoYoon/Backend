package com.sangboyoon.accounter.web.bookmark.dto;

import com.sangboyoon.accounter.domain.corporation.Corporation;
import com.sangboyoon.accounter.domain.user.User;
import lombok.Getter;

@Getter
public class AddBookmarkRequest {
    private String corpCode;

    public AddBookmarkDto convert(User user, Corporation corporation) {
        return new AddBookmarkDto(
                user,
                corporation
        );
    }
}
