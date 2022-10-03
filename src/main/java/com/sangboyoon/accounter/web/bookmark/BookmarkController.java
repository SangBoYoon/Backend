package com.sangboyoon.accounter.web.bookmark;

import com.sangboyoon.accounter.application.bookmark.BookmarkUseCase;
import com.sangboyoon.accounter.application.corporation.CorporationUseCase;
import com.sangboyoon.accounter.configuration.security.CurrentUser;
import com.sangboyoon.accounter.domain.bookmark.Bookmark;
import com.sangboyoon.accounter.domain.corporation.Corporation;
import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.web.bookmark.dto.AddBookmarkRequest;
import com.sangboyoon.accounter.web.bookmark.dto.AddBookmarkResponse;
import com.sangboyoon.accounter.web.bookmark.dto.DeleteBookmarkRequest;
import com.sangboyoon.accounter.web.bookmark.dto.GetBookmarkResponse;
import com.sangboyoon.accounter.web.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {
    private final BookmarkUseCase bookmarkUseCase;
    private final CorporationUseCase corporationUseCase;

    @PostMapping("/bookmark")
    public ApiResponse<AddBookmarkResponse> bookmarkAdd(@CurrentUser User user, @RequestBody AddBookmarkRequest request) {
        Optional<Corporation> corporation = corporationUseCase.findCorporation(request.getCorpCode());
        Bookmark bookmark = bookmarkUseCase.addBookmark(request.convert(user, corporation.get()));
        return new ApiResponse<>(new AddBookmarkResponse(bookmark.getId()));
    }

    @GetMapping("/bookmark")
    public ApiResponse<List<GetBookmarkResponse>> findBookmark(@CurrentUser User user) {
        List<Bookmark> bookmarks = bookmarkUseCase.findBookmarkByUserId(user.getId());

        return new ApiResponse<>(bookmarks.stream()
                .map(GetBookmarkResponse::new)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/bookmark")
    public ApiResponse<String> cancelBookmark(@CurrentUser User user, @RequestBody DeleteBookmarkRequest request) throws Exception {
        bookmarkUseCase.cancelBookmark(user, request.getCorpCode());
        return new ApiResponse<>("delete success");
    }
}
