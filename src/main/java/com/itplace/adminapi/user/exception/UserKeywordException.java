package com.itplace.adminapi.user.exception;

import com.itplace.adminapi.common.BaseCode;
import com.itplace.adminapi.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class UserKeywordException extends BusinessException {
    private final BaseCode code;

    public UserKeywordException(BaseCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
