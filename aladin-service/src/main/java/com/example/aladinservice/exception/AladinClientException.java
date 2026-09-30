package com.example.aladinservice.exception;

import com.example.common.ErrorCode;
import com.example.common.exception.BusinessException;

public class AladinClientException extends BusinessException {
    public AladinClientException(Throwable cause) {
        super(ErrorCode.ALADIN_CLIENT_ERROR, cause);
    }
}
