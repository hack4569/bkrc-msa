package com.example.aladinservice.application.response;

import java.time.LocalDateTime;

public record HistoryResponse(Long historyId, Long memberId, Integer itemId, LocalDateTime createdAt) {
}
