package com.example.userservice.application.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 정보 응답")
public record MemberInfoResponse(
        @Schema(description = "로그인 ID", example = "user123") String loginId
) {
    public static MemberInfoResponse of(String loginId) {
        return new MemberInfoResponse(loginId);
    }
}
