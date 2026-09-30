package com.example.aladinservice.application.response;

import java.util.List;

public record AladinBookPageResponse(List<AladinBookResponse> books, int count) {
}
