package com.itplace.adminapi.benefit.exception;

import com.itplace.adminapi.common.BaseCode;
import com.itplace.adminapi.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class NoBenefitUpdateException extends BusinessException {
    private final BaseCode code;

    public NoBenefitUpdateException(BaseCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
