package com.example.aladinservice.application;

import com.example.aladinservice.application.response.AladinBookPageResponse;
import com.example.aladinservice.application.response.AladinBookResponse;
import com.example.aladinservice.application.response.AladinBookSearchResponse;
import com.example.aladinservice.client.AladinClient;
import com.example.aladinservice.entity.AladinBook;
import com.example.aladinservice.exception.AladinClientException;
import com.example.aladinservice.infrastructure.AladinBookCache;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AladinService {
    private final AladinClient aladinClient;
    private final AladinBookRepository aladinBookRepository;
    private final AladinBookCache cache;

    public AladinBookPageResponse findAll() {
        return cache.find().orElseGet(() -> {
            List<AladinBookResponse> books = aladinBookRepository.findAllWithBookComments().stream()
                    .map(AladinBookResponse::from)
                    .toList();
            AladinBookPageResponse response = new AladinBookPageResponse(books, books.size());
            cache.save(response);
            return response;
        });
    }

    @Retry(name = "aladin")
    @CircuitBreaker(name = "aladin", fallbackMethod = "searchFallback")
    public List<AladinBookSearchResponse> search(String query) {
        return aladinClient.search(query).stream().map(AladinBookSearchResponse::from).toList();
    }

    @Transactional
    @Retry(name = "aladin")
    @CircuitBreaker(name = "aladin", fallbackMethod = "importFallback")
    public AladinBookResponse importByIsbn(String isbn13) {
        AladinBook saved = aladinBookRepository.save(aladinClient.detail(isbn13));
        cache.evict();
        return AladinBookResponse.from(saved);
    }

    private List<AladinBookSearchResponse> searchFallback(String query, Throwable throwable) {
        log.warn("[알라딘] 검색 fallback query={}", query, throwable);
        return List.of();
    }

    private AladinBookResponse importFallback(String isbn13, Throwable throwable) {
        if (throwable instanceof AladinClientException exception) {
            throw exception;
        }
        throw new AladinClientException(throwable);
    }
}
