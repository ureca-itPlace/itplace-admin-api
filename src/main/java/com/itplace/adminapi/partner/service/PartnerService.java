package com.itplace.adminapi.partner.service;

import com.itplace.adminapi.partner.dto.UsageRankResponse;
import java.util.List;

public interface PartnerService {
    List<UsageRankResponse> usageRank(int days);
}
