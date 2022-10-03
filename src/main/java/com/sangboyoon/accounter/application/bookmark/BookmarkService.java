package com.sangboyoon.accounter.application.bookmark;

import com.sangboyoon.accounter.domain.bookmark.Bookmark;
import com.sangboyoon.accounter.domain.bookmark.BookmarkRepository;
import com.sangboyoon.accounter.domain.bookmark.exception.BookmarkNotFoundException;
import com.sangboyoon.accounter.domain.corporation.Corporation;
import com.sangboyoon.accounter.domain.corporation.CorporationRepository;
import com.sangboyoon.accounter.domain.corporation.exception.CCorporationNotFoundException;
import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.domain.user.UserRepository;
import com.sangboyoon.accounter.domain.user.exceptions.CUserNotFoundException;
import com.sangboyoon.accounter.web.bookmark.dto.AddBookmarkDto;
import com.sangboyoon.accounter.web.bookmark.dto.AddBookmarkRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookmarkService implements BookmarkUseCase {
    @Autowired
    private final BookmarkRepository bookmarkRepository;
    private final CorporationRepository corporationRepository;
    private final UserRepository userRepository;

    @Override
    public AddBookmarkDto addBookmark(Long userId, AddBookmarkRequest addBookmarkRequest) {
        User user = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        Corporation corporation = corporationRepository.findByCorpCode(addBookmarkRequest.getCorpCode()).orElseThrow(CCorporationNotFoundException::new);

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .corporation(corporation)
                .build();
        return new AddBookmarkDto(bookmarkRepository.save(bookmark));
    }

    @Override
    public List<AddBookmarkDto> findBookmarkByUserId(Long userId) {
        return bookmarkRepository.findAllByUserId(userId);
    }

    @Override
    public void cancelBookmark(User user, String corpCode) {
        Corporation corporation = corporationRepository.findByCorpCode(corpCode).orElseThrow(CCorporationNotFoundException::new);

        Bookmark bookmark = bookmarkRepository.findByCorporationAndUserId(corporation.getCorpCode(), user.getId()).orElseThrow();
        bookmarkRepository.deleteById(bookmark.getId());
    }
}
