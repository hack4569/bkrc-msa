package com.example.historyservice.entity;

import com.example.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "histories",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_history_item_member",
                columnNames = {"member_id", "item_id"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class History extends BaseEntity {

    @Id
    @Column(name = "history_id", nullable = false)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    public static History create(Integer itemId, Long memberId, Long historyId) {
        return new History(historyId, memberId, itemId);
    }
}
