package com.example.common.exception;

import com.example.common.ErrorCode;
import lombok.Getter;

@Getter
public class MemberNotFoundException extends BusinessException {

    private final Long memberId;

    public MemberNotFoundException(Long memberId) {
        super(ErrorCode.USER_NOT_FOUND);
        this.memberId = memberId;
    }
}
