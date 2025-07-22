package com.itplace.adminapi.history.service.impl;

import com.itplace.adminapi.benefit.entity.enums.Grade;
import com.itplace.adminapi.history.dto.MembershipHistoryResponse;
import com.itplace.adminapi.history.dto.MembershipUsage;
import com.itplace.adminapi.history.entity.MembershipHistory;
import com.itplace.adminapi.history.repository.MembershipHistoryRepository;
import com.itplace.adminapi.history.service.MembershipHistoryService;
import com.itplace.adminapi.user.UserCode;
import com.itplace.adminapi.user.entity.Membership;
import com.itplace.adminapi.user.entity.User;
import com.itplace.adminapi.user.exception.UserNotFoundException;
import com.itplace.adminapi.user.repository.MembershipRepository;
import com.itplace.adminapi.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipHistoryServiceImpl implements MembershipHistoryService {

    private final UserRepository userRepository;
    private final MembershipHistoryRepository membershipHistoryRepository;
    private final MembershipRepository membershipRepository;

    @Override
    public MembershipHistoryResponse getUserBenefitUsage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserCode.USER_NOT_FOUND));

        String membershipId = user.getMembershipId();
        Grade membershipGrade = null;
        List<MembershipUsage> usageList = new ArrayList<>();

        // 2. 멤버십이 있을 경우 처리
        if (membershipId != null) {
            Optional<Membership> membershipOpt = membershipRepository.findById(membershipId);

            if (membershipOpt.isPresent()) {
                membershipGrade = membershipOpt.get().getGrade();

                List<MembershipHistory> histories =
                        membershipHistoryRepository.findByMembership_MembershipIdOrderByUsedAtDesc(membershipId);

                usageList = histories.stream()
                        .map(h -> MembershipUsage.builder()
                                .benefitId(h.getBenefit().getBenefitId())
                                .benefitName(h.getBenefit().getBenefitName())
                                .usedAt(h.getUsedAt().toLocalDate())
                                .discountAmount(h.getDiscountAmount())
                                .build())
                        .collect(Collectors.toList());
            }
        }

        // 3. 응답 DTO 생성
        return MembershipHistoryResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .membershipId(membershipId)
                .membershipGrade(membershipGrade)
                .membershipUsage(usageList)
                .build();
    }
}

