package com.sangboyoon.accounter.web.bookmark.dto;

import com.sangboyoon.accounter.domain.corporation.Corporation;
import com.sangboyoon.accounter.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddBookmarkDto {
    private User user;
    private Corporation corporation;
}
