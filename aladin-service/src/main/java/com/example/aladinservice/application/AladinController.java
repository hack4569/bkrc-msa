package com.example.aladinservice.application;

import com.example.aladinservice.application.response.AladinBookPageResponse;
import com.example.aladinservice.application.response.AladinBookResponse;
import com.example.aladinservice.application.response.AladinBookSearchResponse;
import com.example.aladinservice.application.response.RecommendView;
import com.example.common.security.GatewayMemberAuthenticationFilter;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/aladin-service/v1/aladin/books")
public class AladinController {
    private final AladinService aladinService;
    private final BookRecommendationService recommendationService;

    @GetMapping
    public AladinBookPageResponse getAllBooks() {
        return aladinService.findAll();
    }

    @GetMapping("/search")
    public List<AladinBookSearchResponse> searchBooks(@RequestParam @NotBlank String query) {
        return aladinService.search(query.trim());
    }

    @GetMapping("/recommend/user")
    public List<RecommendView> recommendForUser(
            @RequestHeader(value = GatewayMemberAuthenticationFilter.MEMBER_ID_HEADER, required = false) Long memberId) {
        return recommendationService.recommend(memberId);
    }

    @PostMapping("/import/{isbn13}")
    @ResponseStatus(HttpStatus.CREATED)
    public AladinBookResponse importBook(@PathVariable @NotBlank String isbn13) {
        return aladinService.importByIsbn(isbn13);
    }
}
