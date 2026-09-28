package com.example.userservice.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberRegisterRequest(
        @NotBlank String loginId,
        @NotBlank
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다")
        String password,
        @NotBlank String passwordCheck) {
}
