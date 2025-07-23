package com.itplace.adminapi.benefit.service;

import com.itplace.adminapi.benefit.dto.BenefitDetailResponse;
import com.itplace.adminapi.benefit.dto.BenefitUpdateRequest;
import com.itplace.adminapi.benefit.dto.FavoriteRankResponse;
import java.util.List;

public interface BenefitService {
    List<FavoriteRankResponse> favoriteRank(int limit);
    BenefitDetailResponse getBenefitDetail(Long benefitId);
    void updateBenefitInfo(Long benefitId, BenefitUpdateRequest request);
    Long getBenefitCount();
}

