package com.example.likeservice.application;

import com.example.likeservice.entity.LikeCount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeCountRepository extends JpaRepository<LikeCount, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select count from LikeCount count where count.itemId = :itemId")
    Optional<LikeCount> findByItemIdForUpdate(@Param("itemId") Integer itemId);
}
