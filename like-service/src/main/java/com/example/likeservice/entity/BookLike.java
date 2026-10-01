package com.example.likeservice.entity;

import com.example.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "like_book", uniqueConstraints = @UniqueConstraint(
        name = "uk_like_book_item_member", columnNames = {"item_id", "member_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookLike extends BaseEntity {
    @Id
    @Column(name = "like_id", nullable = false)
    private Long likeId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    private BookLike(Long likeId, Integer itemId, Long memberId) {
        this.likeId = likeId;
        this.itemId = itemId;
        this.memberId = memberId;
    }

    public static BookLike create(Long likeId, Integer itemId, Long memberId) {
        return new BookLike(likeId, itemId, memberId);
    }
}
