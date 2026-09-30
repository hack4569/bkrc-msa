package com.example.aladinservice.application;

import com.example.aladinservice.application.response.AladinBookPageResponse;
import com.example.aladinservice.application.response.AladinBookResponse;
import com.example.aladinservice.application.response.HistoryResponse;
import com.example.aladinservice.client.HistoryClient;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class BookRecommendationServiceTest {
    @Test
    void excludesBooksReadBeforeToday() {
        AladinService aladinService = mock(AladinService.class);
        HistoryClient historyClient = mock(HistoryClient.class);
        AladinBookResponse oldBook = book(1, "old");
        AladinBookResponse newBook = book(2, "new");
        when(aladinService.findAll()).thenReturn(new AladinBookPageResponse(List.of(oldBook, newBook), 2));
        when(historyClient.findByMemberId(10L)).thenReturn(List.of(
                new HistoryResponse(1L, 10L, 1, LocalDateTime.now().minusDays(1))));

        BookRecommendationService service = new BookRecommendationService(aladinService, historyClient);

        assertThat(service.recommend(10L)).extracting(view -> view.itemId()).containsExactly(2);
    }

    @Test
    void recommendsBooksWithoutHistoryForAnonymousUser() {
        AladinService aladinService = mock(AladinService.class);
        HistoryClient historyClient = mock(HistoryClient.class);
        AladinBookResponse book = book(1, "book");
        when(aladinService.findAll()).thenReturn(new AladinBookPageResponse(List.of(book), 1));

        BookRecommendationService service = new BookRecommendationService(aladinService, historyClient);

        assertThat(service.recommend(null)).extracting(view -> view.itemId()).containsExactly(1);
        verifyNoInteractions(historyClient);
    }

    private AladinBookResponse book(int id, String title) {
        return new AladinBookResponse(id, title, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, List.of());
    }
}
