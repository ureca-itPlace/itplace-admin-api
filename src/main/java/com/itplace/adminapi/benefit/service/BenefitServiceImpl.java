package com.itplace.adminapi.benefit.service;

import com.itplace.adminapi.benefit.BenefitCode;
import com.itplace.adminapi.benefit.dto.BenefitDetailResponse;
import com.itplace.adminapi.benefit.dto.BenefitUpdateRequest;
import com.itplace.adminapi.benefit.dto.FavoriteRankResponse;
import com.itplace.adminapi.benefit.dto.TierBenefitInfo;
import com.itplace.adminapi.benefit.entity.Benefit;
import com.itplace.adminapi.benefit.exception.BenefitNotFoundException;
import com.itplace.adminapi.benefit.exception.NoBenefitUpdateException;
import com.itplace.adminapi.benefit.repository.BenefitRepository;
import com.itplace.adminapi.favorite.repository.FavoriteRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BenefitServiceImpl implements BenefitService {

    private final FavoriteRepository favoriteRepository;
    private final BenefitRepository benefitRepository;


    @Override
    public List<FavoriteRankResponse> favoriteRank(int limit) {
        return favoriteRepository.findFavoriteRank(limit).stream()
                .map(f -> new FavoriteRankResponse(
                        f.getBenefitId(),
                        f.getPartnerName(),
                        f.getMainCategory(),
                        f.getFavoriteCount(),
                        f.getFavoriteRank()
                ))
                .toList();
    }

    @Override
    public BenefitDetailResponse getBenefitDetail(Long benefitId) {
        Benefit benefit = benefitRepository.findBenefitWithPartnerById(benefitId)
                .orElseThrow(() -> new BenefitNotFoundException(BenefitCode.BENEFIT_NOT_FOUND));

        List<TierBenefitInfo> tierBenefitInfos = benefit.getTierBenefits().stream()
                .map(tb -> new TierBenefitInfo(
                        tb.getGrade(),
                        tb.getContext(),
                        tb.getIsAll()
                ))
                .toList();

        return BenefitDetailResponse.builder()
                .benefitId(benefit.getBenefitId())
                .benefitName(benefit.getBenefitName())
                .description(benefit.getDescription())
                .benefitLimit(benefit.getBenefitLimit().trim())
                .manual(benefit.getManual().trim())
                .url(benefit.getUrl().trim())
                .partnerName(benefit.getPartner().getPartnerName())
                .image(benefit.getPartner().getImage())
                .tierBenefits(tierBenefitInfos)
                .build();
    }

    @Override
    public void updateBenefitInfo(Long benefitId, BenefitUpdateRequest request) {
        Benefit benefit = benefitRepository.findById(benefitId)
                .orElseThrow(() -> new BenefitNotFoundException(BenefitCode.BENEFIT_NOT_FOUND));

        boolean updated = false;

        if (request.getBenefitLimit() != null) {
            String trimmed = request.getBenefitLimit().trim();
            if (!trimmed.equals(benefit.getBenefitLimit())) {
                benefit.setBenefitLimit(trimmed);
                updated = true;
            }
        }

        if (request.getManual() != null) {
            String trimmed = request.getManual().trim();
            if (!trimmed.equals(benefit.getManual())) {
                benefit.setManual(trimmed);
                updated = true;
            }
        }

        if (!updated) {
            throw new NoBenefitUpdateException(BenefitCode.BENEFIT_NOT_UPDATED);
        }

        benefitRepository.save(benefit);
    }

    @Override
    public Long getBenefitCount() {
        return benefitRepository.count();
    }
}
