package com.example.likeservice.application;

import com.example.likeservice.entity.BookLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<BookLike, Long> {
    Optional<BookLike> findByItemIdAndMemberId(Integer itemId, Long memberId);

    List<BookLike> findAllByMemberIdOrderByLikeIdDesc(Long memberId);
}
