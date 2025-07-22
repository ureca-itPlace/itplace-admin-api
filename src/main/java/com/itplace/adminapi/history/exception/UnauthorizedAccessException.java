package com.itplace.adminapi.history.exception;

import com.itplace.adminapi.common.BaseCode;
import com.itplace.adminapi.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class UnauthorizedAccessException extends BusinessException {
    private final BaseCode code;

    public UnauthorizedAccessException(BaseCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
