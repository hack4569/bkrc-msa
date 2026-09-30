package com.example.aladinservice.application.response;

import com.example.aladinservice.entity.BookComment;

public record BookCommentResponse(Long id, String comment, String type) {
    public static BookCommentResponse from(BookComment source) {
        return new BookCommentResponse(source.getBookCommentId(), source.getComment(), source.getType());
    }
}
