package com.example.userservice.application.request;

import jakarta.validation.constraints.NotBlank;

public record MemberWithdrawRequest(
        @NotBlank String password) {
}