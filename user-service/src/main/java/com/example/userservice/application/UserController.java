package com.example.userservice.application;

import com.example.userservice.application.request.MemberModifyRequest;
import com.example.userservice.application.request.MemberRegisterRequest;
import com.example.userservice.application.request.MemberWithdrawRequest;
import com.example.userservice.application.response.MemberInfoResponse;
import com.example.userservice.application.response.MemberModifyResponse;
import com.example.userservice.application.response.MemberRegisterResponse;
import com.example.common.security.GatewayMemberAuthenticationFilter;
import com.example.userservice.entity.PasswordEncoder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 (Member)", description = "회원 가입 및 정보 조회 API")
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

    @Operation(summary = "회원 가입", description = "새로운 회원을 등록합니다.", security = {})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
            @ApiResponse(responseCode = "400", description = """
                    USER_ALREADY_EXISTS: 이미 등록된 사용자 입니다.
                    USER_NOT_EQUALS_PW: 비밀번호가 일치하지 않습니다.""",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
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
    @Operation(summary = "회원 정보 조회", description = "인증된 회원의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "USER_NOT_FOUND: 해당 아이디를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public MemberInfoResponse getMemberInfo(
            @Parameter(hidden = true)
            @RequestHeader(GatewayMemberAuthenticationFilter.MEMBER_ID_HEADER) Long memberId) {
        return userService.getMemberInfo(memberId);
    }

//    @DeleteMapping("/v1/member/me")
//    public void withdraw(
//            @AuthenticationPrincipal Long memberId,
//            @RequestBody @Valid MemberWithdrawRequest request) {
//        userService.withdrawMember(memberId, request);
//    }
}
