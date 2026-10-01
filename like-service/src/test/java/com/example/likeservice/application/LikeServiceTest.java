package com.example.likeservice.application;

import com.example.common.ErrorCode;
import com.example.common.Snowflake;
import com.example.common.exception.BusinessException;
import com.example.likeservice.entity.BookLike;
import com.example.likeservice.entity.LikeCount;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LikeServiceTest {
    private final LikeRepository likeRepository = mock(LikeRepository.class);
    private final LikeCountRepository likeCountRepository = mock(LikeCountRepository.class);
    private final Snowflake snowflake = mock(Snowflake.class);
    private final LikeService likeService = new LikeService(likeRepository, likeCountRepository, snowflake);

    @Test
    void createsLikeAndIncreasesCount() {
        when(likeRepository.findByItemIdAndMemberId(10, 20L)).thenReturn(Optional.empty());
        when(snowflake.nextId()).thenReturn(30L);
        when(likeCountRepository.findByItemIdForUpdate(10)).thenReturn(Optional.empty());

        assertThat(likeService.like(10, 20L).itemId()).isEqualTo(10);
        verify(likeRepository).saveAndFlush(any(BookLike.class));
        verify(likeCountRepository).save(any(LikeCount.class));
    }

    @Test
    void rejectsDuplicateLike() {
        when(likeRepository.findByItemIdAndMemberId(10, 20L))
                .thenReturn(Optional.of(BookLike.create(30L, 10, 20L)));

        assertThatThrownBy(() -> likeService.like(10, 20L))
                .isInstanceOfSatisfying(BusinessException.class,
                        exception -> assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.LIKE_ALREADY_EXISTS));
    }

    @Test
    void rejectsLikeWhenMemberIdIsMissing() {
        assertThatThrownBy(() -> likeService.like(10, null))
                .isInstanceOfSatisfying(BusinessException.class,
                        exception -> assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.AUTHENTICATION_FAILED));
    }

    @Test
    void deletesLikeAndDecreasesCount() {
        BookLike bookLike = BookLike.create(30L, 10, 20L);
        LikeCount count = LikeCount.create(10);
        count.increase();
        when(likeRepository.findByItemIdAndMemberId(10, 20L)).thenReturn(Optional.of(bookLike));
        when(likeCountRepository.findByItemIdForUpdate(10)).thenReturn(Optional.of(count));

        likeService.unlike(10, 20L);

        verify(likeRepository).delete(bookLike);
        assertThat(count.getLikeCount()).isZero();
    }
}
