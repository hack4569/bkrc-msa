package com.example.aladinservice.application.response;

import com.example.aladinservice.entity.AladinBook;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AladinResponse(
        Integer totalResults,
        Integer startIndex,
        Integer itemsPerPage,
        List<AladinBook> item
) {
}
