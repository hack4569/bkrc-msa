package com.example.aladinservice.application.response;

import java.util.List;

public record RecommendView(
        Integer itemId,
        List<BookCommentResponse> recommendCommentList,
        String title,
        String link,
        String cover,
        String author,
        String categoryName
) {
}
