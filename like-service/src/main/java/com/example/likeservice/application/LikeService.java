package com.example.likeservice.application;

import com.example.common.ErrorCode;
import com.example.common.Snowflake;
import com.example.common.exception.BusinessException;
import com.example.likeservice.application.response.LikeCountResponse;
import com.example.likeservice.application.response.LikeResponse;
import com.example.likeservice.application.response.MyLikeResponse;
import com.example.likeservice.entity.BookLike;
import com.example.likeservice.entity.LikeCount;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    private final LikeRepository likeRepository;
    private final LikeCountRepository likeCountRepository;
    private final Snowflake snowflake;

    @Transactional
    public LikeResponse like(Integer itemId, Long memberId) {
        if (memberId == null) {
            throw new BusinessException(ErrorCode.AUTHENTICATION_FAILED);
        }
        if (likeRepository.findByItemIdAndMemberId(itemId, memberId).isPresent()) {
            throw alreadyProcessed();
        }

        try {
            likeRepository.saveAndFlush(BookLike.create(snowflake.nextId(), itemId, memberId));
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS, exception);
        }

        LikeCount count = likeCountRepository.findByItemIdForUpdate(itemId)
                .orElseGet(() -> LikeCount.create(itemId));
        count.increase();
        likeCountRepository.save(count);
        return new LikeResponse(itemId);
    }

    @Transactional
    public void unlike(Integer itemId, Long memberId) {
        BookLike bookLike = likeRepository.findByItemIdAndMemberId(itemId, memberId)
                .orElseThrow(this::alreadyProcessed);
        likeRepository.delete(bookLike);

        likeCountRepository.findByItemIdForUpdate(itemId).ifPresent(count -> {
            count.decrease();
            likeCountRepository.save(count);
        });
    }

    public List<MyLikeResponse> findMine(Long memberId) {
        return likeRepository.findAllByMemberIdOrderByLikeIdDesc(memberId).stream()
                .map(like -> new MyLikeResponse(like.getLikeId(), like.getItemId()))
                .toList();
    }

    public LikeCountResponse count(Integer itemId) {
        long count = likeCountRepository.findById(itemId).map(LikeCount::getLikeCount).orElse(0L);
        return new LikeCountResponse(itemId, count);
    }

    private BusinessException alreadyProcessed() {
        return new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS);
    }
}
