package com.sangboyoon.accounter.application.bookmark;

import com.sangboyoon.accounter.domain.bookmark.Bookmark;
import com.sangboyoon.accounter.domain.bookmark.BookmarkRepository;
import com.sangboyoon.accounter.domain.bookmark.exception.BookmarkNotFoundException;
import com.sangboyoon.accounter.domain.corporation.Corporation;
import com.sangboyoon.accounter.domain.corporation.CorporationRepository;
import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.web.bookmark.dto.AddBookmarkDto;
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

    @Override
    public Bookmark addBookmark(AddBookmarkDto addBookmarkDto) {
        log.info(addBookmarkDto.getCorporation().getCorpCode());
        Bookmark bookmark = Bookmark.builder()
                .user(addBookmarkDto.getUser())
                .corporation(addBookmarkDto.getCorporation())
                .build();
        return bookmarkRepository.save(bookmark);
    }

    @Override
    public List<Bookmark> findBookmarkByUserId(Long userId) {
        return bookmarkRepository.findAllByUserId(userId);
    }

    @Override
    public void cancelBookmark(User user, String corpCode) {
        Optional<Corporation> corporation = corporationRepository.findByCorpCode(corpCode);

        Bookmark bookmark = bookmarkRepository
                .findByCorporationAndUserId(corporation.get().getCorpCode(), user.getId())
                .orElseThrow(BookmarkNotFoundException::new);
        bookmarkRepository.deleteById(bookmark.getId());
    }
}
