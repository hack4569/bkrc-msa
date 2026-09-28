package com.example.userservice.application.response;

public record MemberInfoResponse(
        String loginId
) {
    public static MemberInfoResponse of(String loginId) {
        return new MemberInfoResponse(loginId);
    }
}
