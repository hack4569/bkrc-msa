package com.example.userservice.application;


import com.example.common.ErrorCode;
import com.example.common.Snowflake;
import com.example.common.exception.BusinessException;
import com.example.userservice.application.request.MemberRegisterRequest;
import com.example.userservice.application.response.MemberInfoResponse;
import com.example.userservice.dto.MemberDto;
import com.example.userservice.entity.Member;
import com.example.userservice.entity.PasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final Snowflake snowflake;

    @Override
    @Transactional
    public Member saveMember(MemberRegisterRequest request) {
        checkDuplicateId(request);
        checkPwd(request.password(), request.passwordCheck());

        var member = Member.register(snowflake.nextId(), request.loginId(), request.password(), passwordEncoder);
        var savedMember = memberRepository.save(member);

        return savedMember;
    }

    private void checkPwd(String pwd, String pwdChk) {
        if (!pwd.equals(pwdChk)) {
            throw new BusinessException(ErrorCode.USER_NOT_EQUALS_PW);
        }
    }

    private void checkDuplicateId(MemberRegisterRequest request) {
        if (memberRepository.findMemberByLoginId(request.loginId()).isPresent()) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }

    @Override
    public MemberDto getMemberByLoginId(String loginId) {
        var member = memberRepository.findMemberByLoginId(loginId);
        if (!member.isPresent()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        MemberDto result = objectMapper.convertValue(member.get(), MemberDto.class);
        return result;
    }

    @Override
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return MemberInfoResponse.of(
            member.getLoginId()
        );

    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var member = memberRepository.findMemberByLoginId(username).orElseThrow( () -> new UsernameNotFoundException(username));

        return new User(member.getLoginId(), member.getPassword(),
                true, true, true, true,
                new ArrayList<>());
    }

//    @Override
//    @Transactional
//    public Member modifyMember(Long memberId, MemberModifyRequest request) {
//        var member = memberRepository.findById(memberId).orElseThrow( () -> new UsernameNotFoundException(request.loginId()));
//        if (!member.checkPassword(request.originPassword(), passwordEncoder)) {
//            throw new BusinessException(ErrorCode.USER_NOT_EQUALS_PW);
//        }
//        this.checkPwd(request.newPassword(), request.newPasswordCheck());
//        member.modify(request.newPassword(), passwordEncoder);
//        var modifiedMember = memberRepository.save(member);
//
//        return modifiedMember;
//    }

//    @Override
//    @Transactional
//    public void withdrawMember(Long memberId, MemberWithdrawRequest request) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
//
//        if (!member.checkPassword(request.password(), passwordEncoder)) {
//            throw new BusinessException(ErrorCode.USER_NOT_EQUALS_PW);
//        }
//        historyRepository.deleteByMemberId(memberId);
//        memberRepository.deleteById(memberId);
//
////        Outbox outbox = outboxRepository.save(Outbox.of(
////                EventType.MEMBER_WITHDRAW,
////                RabbitMQConfig.NOTIFICATION_DIRECT_EXCHANGE,
////                RabbitMQConfig.WITHDRAW_ROUTING_KEY,
////                Event.of(EventType.MEMBER_WITHDRAW, MemberWithdrawEventPayload.builder()
////                        .loginId(member.getLoginId())
////                        .withdrawnAt(java.time.LocalDateTime.now())
////                        .build()).toJson()
////        ));
////        eventPublisher.publishEvent(OutboxEvent.of(outbox));
//    }

}
