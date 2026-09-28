package com.example.userservice.application;


import com.example.userservice.application.request.MemberModifyRequest;
import com.example.userservice.application.request.MemberRegisterRequest;
import com.example.userservice.application.request.MemberWithdrawRequest;
import com.example.userservice.application.response.MemberInfoResponse;
import com.example.userservice.dto.MemberDto;
import com.example.userservice.entity.Member;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Member saveMember(MemberRegisterRequest request);
    MemberDto getMemberByLoginId(String loginId);
    MemberInfoResponse getMemberInfo(Long memberId);
//    Member modifyMember(Long memberId, MemberModifyRequest request);
//    void withdrawMember(Long memberId, MemberWithdrawRequest request);
}
