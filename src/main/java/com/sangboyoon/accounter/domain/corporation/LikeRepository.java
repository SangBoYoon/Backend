package com.sangboyoon.accounter.domain.corporation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    @Query(value = "SELECT * FROM like_entity " +
            "where user_id = :userId AND corp_code = :corporation", nativeQuery = true)
    Optional<LikeEntity> findByCorporationAndUserId(@Param("corporation") String corporation, @Param("userId") Long userId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM accounter.like_entity e WHERE e.corp_code = :corporation AND e.user_id = :userId")
    void deleteByCorporationAndUserIdInQuery(@Param("corporation") String corporation, @Param("userId") Long userId);

    void deleteAllByUserId(Long userId);
}
