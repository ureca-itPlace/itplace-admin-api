package com.itplace.adminapi.partner.service;

import com.itplace.adminapi.history.repository.MembershipHistoryRepository;
import com.itplace.adminapi.partner.dto.UsageRankResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final MembershipHistoryRepository membershipHistoryRepository;

    @Override
    public List<UsageRankResponse> usageRank(int days) {
        return membershipHistoryRepository.findTop5PartnerId(days).stream()
                .map(p -> new UsageRankResponse(
                        p.getPartnerId(),
                        p.getPartnerName(),
                        p.getVvipUsageCount(),
                        p.getVipUsageCount(),
                        p.getBasicUsageCount(),
                        p.getTotalUsageCount()
                ))
                .toList();
    }
}
