package com.example.userservice.application.response;


import com.example.userservice.entity.Member;

public record MemberModifyResponse(
        String loginId) {
    public static MemberModifyResponse of(Member member) {
        return new MemberModifyResponse(member.getLoginId());
    }
}
