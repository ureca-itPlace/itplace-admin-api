package com.itplace.adminapi.benefit.service;

import com.itplace.adminapi.benefit.dto.BenefitDetailResponse;
import com.itplace.adminapi.benefit.dto.FavoriteRankResponse;
import java.util.List;

public interface BenefitService {
    List<FavoriteRankResponse> favoriteRank(int limit);
    BenefitDetailResponse getBenefitDetail(Long benefitId);
}

