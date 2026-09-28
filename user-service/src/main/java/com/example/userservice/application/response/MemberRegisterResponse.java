package com.example.userservice.application.response;

import com.example.userservice.entity.Member;

public record MemberRegisterResponse(
        String loginId) {
    public static MemberRegisterResponse of(Member member) {
        return new MemberRegisterResponse(member.getLoginId());
    }
}
