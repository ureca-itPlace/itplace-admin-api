package com.itplace.adminapi.history.service;

import com.itplace.adminapi.history.dto.MembershipHistoryResponse;

public interface MembershipHistoryService {
    MembershipHistoryResponse getUserBenefitUsage(Long userId);
}

