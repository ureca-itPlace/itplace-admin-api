package com.itplace.adminapi.history.service.impl;

import com.itplace.adminapi.benefit.entity.Benefit;
import com.itplace.adminapi.history.dto.MembershipHistoryResponse;
import com.itplace.adminapi.history.dto.MonthlyDiscountResponse;
import com.itplace.adminapi.history.entity.MembershipHistory;
import com.itplace.adminapi.history.repository.MembershipHistoryRepository;
import com.itplace.adminapi.history.service.MembershipHistoryService;
import com.itplace.adminapi.partner.entity.Partner;
import com.itplace.adminapi.user.UserCode;
import com.itplace.adminapi.user.dto.PagedResponse;
import com.itplace.adminapi.user.entity.User;
import com.itplace.adminapi.user.exception.NoMembershipException;
import com.itplace.adminapi.user.exception.UserNotFoundException;
import com.itplace.adminapi.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipHistoryServiceImpl implements MembershipHistoryService {

    private final UserRepository userRepository;
    private final MembershipHistoryRepository historyRepository;

    @Override
    public PagedResponse<MembershipHistoryResponse> getUserHistory(
            Long userId,
            String keyword,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserCode.USER_NOT_FOUND));

        String membershipId = user.getMembershipId();
        if (membershipId == null) {
            throw new NoMembershipException(UserCode.NO_MEMBERSHIP);
        }

        Page<MembershipHistory> page = historyRepository.findFiltered(
                membershipId,
                keyword,
                startDate != null ? startDate.atStartOfDay() : null,
                endDate != null ? endDate.atTime(LocalTime.MAX) : null,
                pageable
        );

        List<MembershipHistoryResponse> dtoList = page.stream()
                .map(mh -> {
                    Benefit b = mh.getBenefit();
                    Partner p = b.getPartner();
                    return MembershipHistoryResponse.builder()
                            .image(p.getImage())
                            .benefitName(b.getBenefitName())
                            .discountAmount(mh.getDiscountAmount())
                            .usedAt(mh.getUsedAt())
                            .build();
                })
                .toList();

        return new PagedResponse<>(
                dtoList,
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.hasNext()
        );
    }

    @Override
    public MonthlyDiscountResponse getMonthlyDiscountSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserCode.USER_NOT_FOUND));

        String membershipId = user.getMembershipId();
        if (membershipId == null) {
            throw new NoMembershipException(UserCode.NO_MEMBERSHIP);
        }

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        Long totalDiscountAmount = historyRepository.sumDiscountAmountThisMonth(membershipId, year, month);

        return MonthlyDiscountResponse.builder()
                .userId(userId)
                .yearMonth(String.format("%04d-%02d", year, month))
                .totalDiscountAmount(totalDiscountAmount)
                .build();
    }
}

