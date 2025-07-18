package com.itplace.adminapi.benefit.exception;

import com.itplace.adminapi.common.BaseCode;
import com.itplace.adminapi.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class BenefitNotFoundException extends BusinessException {
    private final BaseCode code;

    public BenefitNotFoundException(BaseCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
