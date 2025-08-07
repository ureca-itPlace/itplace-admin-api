package com.itplace.adminapi.benefit.service;

import com.itplace.adminapi.benefit.dto.BenefitDetailResponse;
import com.itplace.adminapi.benefit.dto.BenefitListRequest;
import com.itplace.adminapi.benefit.dto.BenefitResponse;
import com.itplace.adminapi.benefit.dto.BenefitUpdateRequest;
import com.itplace.adminapi.benefit.dto.FavoriteRankResponse;
import com.itplace.adminapi.benefit.dto.PagedResponse;
import java.util.List;

public interface BenefitService {
    List<FavoriteRankResponse> favoriteRank(int limit);
    BenefitDetailResponse getBenefitDetail(Long benefitId);
    void updateBenefitInfo(Long benefitId, BenefitUpdateRequest request);
    Long getBenefitCount();
    PagedResponse<BenefitResponse> getBenefitList(BenefitListRequest request);
}

