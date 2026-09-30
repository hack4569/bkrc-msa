package com.example.historyservice.application;

import com.example.historyservice.entity.History;

import java.time.LocalDateTime;

public record HistoryResponse(
        Long historyId,
        Long memberId,
        Integer itemId,
        LocalDateTime createdAt
) {
    public static HistoryResponse from(History history) {
        return new HistoryResponse(
                history.getId(),
                history.getMemberId(),
                history.getItemId(),
                history.getCreated()
        );
    }
}
