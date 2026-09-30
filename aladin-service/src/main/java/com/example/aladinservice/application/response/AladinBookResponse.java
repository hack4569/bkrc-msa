package com.example.aladinservice.application.response;

import com.example.aladinservice.entity.AladinBook;

import java.util.List;

public record AladinBookResponse(
        Integer itemId,
        String title,
        String link,
        String author,
        String pubDate,
        String description,
        String isbn,
        String isbn13,
        Integer priceSales,
        Integer priceStandard,
        String cover,
        Integer categoryId,
        String categoryName,
        String publisher,
        Integer salesPoint,
        Boolean adult,
        Integer customerReviewRank,
        List<BookCommentResponse> comments
) {
    public static AladinBookResponse from(AladinBook book) {
        List<BookCommentResponse> comments = book.getBookCommentList() == null
                ? List.of()
                : book.getBookCommentList().stream().map(BookCommentResponse::from).toList();
        return new AladinBookResponse(
                book.getItemId(), book.getTitle(), book.getLink(), book.getAuthor(), book.getPubDate(),
                book.getDescription(), book.getIsbn(), book.getIsbn13(), book.getPriceSales(),
                book.getPriceStandard(), book.getCover(), book.getCategoryId(), book.getCategoryName(),
                book.getPublisher(), book.getSalesPoint(), book.getAdult(), book.getCustomerReviewRank(), comments);
    }
}
