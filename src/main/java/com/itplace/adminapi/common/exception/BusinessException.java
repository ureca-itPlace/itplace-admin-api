package com.itplace.adminapi.common.exception;


import com.itplace.adminapi.common.BaseCode;

public abstract class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    // 자식 예외들이 반드시 SecurityCode를 반환하도록 강제
    public abstract BaseCode getCode();
}
