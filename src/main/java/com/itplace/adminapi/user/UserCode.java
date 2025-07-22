package com.itplace.adminapi.user;

import com.itplace.adminapi.common.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserCode implements BaseCode {
    USER_TOTAL_COUNT_SUCCESS("USER_TOTAL_COUNT_SUCCESS", HttpStatus.OK, "전체 사용자 수 조회에 성공했습니다."),
    USER_PAGE_SUCCESS("USER_PAGE_SUCCESS", HttpStatus.OK, "사용자 목록 조회에 성공했습니다."),
    USER_SEARCH_SUCCESS("USER_SEARCH_SUCCESS",HttpStatus.OK,"사용자 검색에 성공했습니다."),
    KEYWORD_REQUIRED("KEYWORD_REQUIRED", HttpStatus.BAD_REQUEST, "검색어를 입력하세요"),
    USER_NOT_FOUND("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
    NO_MEMBERSHIP("NO_MEMBERSHIP", HttpStatus.BAD_REQUEST, "해당 사용자는 멤버십 정보를 가지고 있지 않습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
