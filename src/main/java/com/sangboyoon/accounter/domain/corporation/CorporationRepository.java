package com.sangboyoon.accounter.domain.corporation;

import com.sangboyoon.accounter.domain.bookmark.Bookmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CorporationRepository extends JpaRepository<Corporation, String> {
    List<Corporation> findAll();

    Optional<Corporation> findByCorpCode(String corpCode);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE accounter.corporation m SET m.corp_like = m.corp_like + 1 WHERE corp_code = :corpCode", nativeQuery = true)
    void plusLike(@Param("corpCode") String corpCode);


    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE accounter.corporation m SET m.corp_like = m.corp_like - 1 WHERE corp_code = :corpCode", nativeQuery = true)
    void minusLike(@Param("corpCode") String corpCode);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "INSERT IGNORE INTO  corporation (corp_code, corp_name, corp_category, corp_like) " +
            "VALUES (:corp_code, :corp_name, :corp_category, 0) ", nativeQuery = true)
    void addCorp(@Param("corp_code") String corpCode, @Param("corp_name") String corpName, @Param("corp_category") String corpCategory);
}
