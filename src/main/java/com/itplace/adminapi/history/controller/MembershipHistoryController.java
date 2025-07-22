package com.itplace.adminapi.history.controller;

import com.itplace.adminapi.common.ApiResponse;
import com.itplace.adminapi.history.dto.MembershipHistoryResponse;
import com.itplace.adminapi.history.service.MembershipHistoryService;
import com.itplace.adminapi.user.UserCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MembershipHistoryController {
    private final MembershipHistoryService membershipHistoryService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<MembershipHistoryResponse>> getUserBenefitUsage(@PathVariable Long userId) {
        MembershipHistoryResponse response = membershipHistoryService.getUserBenefitUsage(userId);
        ApiResponse<MembershipHistoryResponse> body = ApiResponse.of(UserCode.USER_DETAIL_SUCCESS, response);
        return ResponseEntity.status(body.getStatus()).body(body);
    }
}
