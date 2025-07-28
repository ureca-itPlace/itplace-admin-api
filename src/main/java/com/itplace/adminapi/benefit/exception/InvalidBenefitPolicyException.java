package com.itplace.adminapi.benefit.exception;

import com.itplace.adminapi.common.BaseCode;
import com.itplace.adminapi.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class InvalidBenefitPolicyException extends BusinessException {
    private final BaseCode code;

    public InvalidBenefitPolicyException(BaseCode code) {
        super(code.getMessage());
        this.code = code;
    }
}