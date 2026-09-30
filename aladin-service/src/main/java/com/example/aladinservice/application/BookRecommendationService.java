package com.example.aladinservice.application;

import com.example.aladinservice.application.response.AladinBookResponse;
import com.example.aladinservice.application.response.BookCommentResponse;
import com.example.aladinservice.application.response.HistoryResponse;
import com.example.aladinservice.application.response.RecommendView;
import com.example.aladinservice.client.HistoryClient;
import com.example.common.ErrorCode;
import com.example.common.RcmdConst;
import com.example.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookRecommendationService {
    private static final Map<String, Integer> COMMENT_ORDER = Map.of(
            "user", 1, "descriptionInsight", 2, "description", 3,
            "aiRecommend", 4, "mdRecommend", 5, "phrase", 6, "toc", 7);

    private final AladinService aladinService;
    private final HistoryClient historyClient;

    public List<RecommendView> recommend(Long memberId) {
        List<AladinBookResponse> books = aladinService.findAll().books();
        if (books.isEmpty()) {
            throw new BusinessException(ErrorCode.ALADIN_NOT_FOUND);
        }

        List<HistoryResponse> histories = memberId == null
                ? List.of()
                : historyClient.findByMemberId(memberId);
        List<AladinBookResponse> filtered = books.stream()
                .filter(book -> wasNotPreviouslyRead(book.itemId(), histories))
                .toList();

        if (filtered.isEmpty() && memberId != null) {
            historyClient.deleteByMemberId(memberId);
            filtered = books;
        }

        return filtered.stream().limit(RcmdConst.SHOW_BOOKS_COUNT).map(this::toView).toList();
    }

    private boolean wasNotPreviouslyRead(Integer itemId, List<HistoryResponse> histories) {
        return histories.stream().noneMatch(history -> Objects.equals(itemId, history.itemId())
                && history.createdAt() != null
                && !LocalDate.now().isEqual(history.createdAt().toLocalDate()));
    }

    private RecommendView toView(AladinBookResponse book) {
        List<BookCommentResponse> comments = book.comments().stream()
                .sorted(Comparator.comparingInt(comment -> COMMENT_ORDER.getOrDefault(comment.type(), Integer.MAX_VALUE)))
                .toList();
        return new RecommendView(book.itemId(), comments, book.title(), book.link(), book.cover(),
                formatAuthor(book.author()), formatCategory(book.categoryName()));
    }

    private String formatAuthor(String author) {
        if (author == null || !author.contains(",")) {
            return author;
        }
        String[] authors = author.split(",");
        return authors[0] + " 외 " + (authors.length - 1) + "명";
    }

    private String formatCategory(String category) {
        if (category == null || !category.contains(">")) {
            return category;
        }
        String[] categories = category.split(">");
        return categories.length > 1 ? categories[1] : category;
    }
}
