package com.itplace.adminapi.security;

import com.itplace.adminapi.common.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityCode implements BaseCode {

    LOGIN_SUCCESS("LOGIN_SUCCESS", HttpStatus.OK, "성공적으로 로그인 되었습니다."),
    LOGOUT_SUCCESS("LOGOUT_SUCCESS", HttpStatus.OK, "성공적으로 로그아웃 되었습니다."),
    LOGIN_FAIL("LOGIN_FAIL", HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
