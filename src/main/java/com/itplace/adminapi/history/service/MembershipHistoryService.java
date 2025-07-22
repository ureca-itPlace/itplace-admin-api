package com.itplace.adminapi.history.service;

import com.itplace.adminapi.history.dto.MembershipHistoryResponse;
import com.itplace.adminapi.history.dto.MonthlyDiscountResponse;
import com.itplace.adminapi.user.dto.PagedResponse;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;

public interface MembershipHistoryService {
    PagedResponse<MembershipHistoryResponse> getUserHistory(
            Long userId,
            String keyword,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    MonthlyDiscountResponse getMonthlyDiscountSummary(Long userId);
}

