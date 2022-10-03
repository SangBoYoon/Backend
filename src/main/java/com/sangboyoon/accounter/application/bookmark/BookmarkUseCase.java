package com.sangboyoon.accounter.application.bookmark;

import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.web.bookmark.dto.AddBookmarkDto;
import com.sangboyoon.accounter.web.bookmark.dto.AddBookmarkRequest;

import java.util.List;

public interface BookmarkUseCase {
    AddBookmarkDto addBookmark(Long userId, AddBookmarkRequest addBookmarkRequest);

    List<AddBookmarkDto> findBookmarkByUserId(Long userId);

    void cancelBookmark(User user, String corpCode) throws Exception;
}
