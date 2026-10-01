package com.example.likeservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "like_count_book")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeCount {
    @Id
    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(name = "like_count", nullable = false)
    private long likeCount;

    @Version
    @Column(name = "event_version", nullable = false)
    private long eventVersion;

    private LikeCount(Integer itemId) {
        this.itemId = itemId;
    }

    public static LikeCount create(Integer itemId) {
        return new LikeCount(itemId);
    }

    public void increase() {
        likeCount++;
    }

    public void decrease() {
        likeCount = Math.max(0, likeCount - 1);
    }
}
