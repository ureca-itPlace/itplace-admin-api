package com.itplace.adminapi.partner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsageRankResponse {
    private Long partnerId;
    private String partnerName;
    private Long vvipUsageCount;
    private Long vipUsageCount;
    private Long basicUsageCount;
    private Long totalUsageCount;
}
