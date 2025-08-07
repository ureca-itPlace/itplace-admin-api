package com.itplace.adminapi.partner;

import com.itplace.adminapi.common.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PartnerCode implements BaseCode {
    PARTNER_USAGE_SUCCESS("PARTNER_USAGE_SUCCESS", HttpStatus.OK, "제휴처별 이용 통계가 성공적으로 조회되었습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
