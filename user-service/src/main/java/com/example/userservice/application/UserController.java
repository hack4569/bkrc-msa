package com.example.userservice.application;

import com.example.userservice.application.request.MemberModifyRequest;
import com.example.userservice.application.request.MemberRegisterRequest;
import com.example.userservice.application.request.MemberWithdrawRequest;
import com.example.userservice.application.response.MemberInfoResponse;
import com.example.userservice.application.response.MemberModifyResponse;
import com.example.userservice.application.response.MemberRegisterResponse;
import com.example.userservice.entity.PasswordEncoder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-service")
public class UserController {
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to UserService!";
    }
    @PostMapping("/v1/member")
    public MemberRegisterResponse register(@RequestBody @Valid MemberRegisterRequest request) {
        var member = userService.saveMember(request);
        var registeredMember = MemberRegisterResponse.of(member);
        return registeredMember;
    }

//    @PutMapping("/v1/member/me")
//    public MemberModifyResponse update(
//            @AuthenticationPrincipal Long memberId,
//            @RequestBody @Valid MemberModifyRequest request) {
//        var response = userService.modifyMember(memberId, request);
//        return MemberModifyResponse.of(response);
//    }

    @GetMapping("/v1/member/me")
    public MemberInfoResponse getMemberInfo(
            @AuthenticationPrincipal Long memberId) {
        return userService.getMemberInfo(memberId);
    }

//    @DeleteMapping("/v1/member/me")
//    public void withdraw(
//            @AuthenticationPrincipal Long memberId,
//            @RequestBody @Valid MemberWithdrawRequest request) {
//        userService.withdrawMember(memberId, request);
//    }
}
