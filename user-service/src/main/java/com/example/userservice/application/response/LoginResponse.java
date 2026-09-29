package com.example.userservice.application.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record LoginResponse(
        @Schema(description = "JWT 인증 토큰") String token,
        @Schema(description = "로그인 ID", example = "user123") String loginId) {
}
