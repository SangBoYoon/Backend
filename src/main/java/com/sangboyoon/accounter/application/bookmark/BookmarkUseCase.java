package com.sangboyoon.accounter.application.bookmark;

import com.sangboyoon.accounter.domain.bookmark.Bookmark;
import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.web.bookmark.dto.AddBookmarkDto;

import java.util.List;

public interface BookmarkUseCase {
    Bookmark addBookmark(AddBookmarkDto addBookmarkDto);

    List<Bookmark> findBookmarkByUserId(Long userId);

    void cancelBookmark(User user, String corpCode) throws Exception;
}
