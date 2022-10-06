package com.sangboyoon.accounter.domain.bookmark;

import com.sangboyoon.accounter.web.bookmark.dto.AddBookmarkDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<AddBookmarkDto> findAllByUserId(Long userId);

    @Query(value = "SELECT * FROM bookmark " +
            "where user_id = :userId AND corp_code = :corporation", nativeQuery = true)
    Optional<Bookmark> findByCorporationAndUserId(@Param("corporation") String corporation, @Param("userId") Long userId);

    void deleteAllByUserId(Long userId);
}
