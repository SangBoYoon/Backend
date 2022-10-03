package com.sangboyoon.accounter.web.bookmark;

import com.sangboyoon.accounter.application.bookmark.BookmarkUseCase;
import com.sangboyoon.accounter.application.corporation.CorporationUseCase;
import com.sangboyoon.accounter.configuration.security.CurrentUser;
import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.web.bookmark.dto.*;
import com.sangboyoon.accounter.web.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounter")
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {
    private final BookmarkUseCase bookmarkUseCase;
    private final CorporationUseCase corporationUseCase;

    @PostMapping("/bookmark")
    public ApiResponse<AddBookmarkResponse> bookmarkAdd(@CurrentUser User user, @RequestBody AddBookmarkRequest request) {
        AddBookmarkDto bookmark = bookmarkUseCase.addBookmark(user.getId(), request);

        return new ApiResponse<>(new AddBookmarkResponse(bookmark.getUser(), bookmark.getCorporation()));
    }

    @GetMapping("/bookmark")
    public ApiResponse<List<GetBookmarkResponse>> findBookmark(@CurrentUser User user) {
        List<AddBookmarkDto> bookmarks = bookmarkUseCase.findBookmarkByUserId(user.getId());

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
