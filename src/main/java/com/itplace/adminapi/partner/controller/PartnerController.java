package com.itplace.adminapi.partner.controller;

import com.itplace.adminapi.common.ApiResponse;
import com.itplace.adminapi.partner.PartnerCode;
import com.itplace.adminapi.partner.dto.UsageRankResponse;
import com.itplace.adminapi.partner.service.PartnerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping("/usage")
    public ResponseEntity<ApiResponse<?>> getPartnerUsage(@RequestParam(defaultValue = "365") int days) {
        List<UsageRankResponse> usageRank = partnerService.usageRank(days);
        ApiResponse<?> body = ApiResponse.of(PartnerCode.PARTNER_USAGE_SUCCESS, usageRank);

        return ResponseEntity.status(body.getStatus()).body(body);
    }
}
