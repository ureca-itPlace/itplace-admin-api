package com.itplace.adminapi.benefit.service;

import com.itplace.adminapi.benefit.BenefitCode;
import com.itplace.adminapi.benefit.dto.BenefitDetailResponse;
import com.itplace.adminapi.benefit.dto.BenefitListRequest;
import com.itplace.adminapi.benefit.dto.BenefitResponse;
import com.itplace.adminapi.benefit.dto.BenefitUpdateRequest;
import com.itplace.adminapi.benefit.dto.FavoriteRankResponse;
import com.itplace.adminapi.benefit.dto.PagedResponse;
import com.itplace.adminapi.benefit.dto.TierBenefitInfo;
import com.itplace.adminapi.benefit.entity.Benefit;
import com.itplace.adminapi.benefit.entity.BenefitPolicy;
import com.itplace.adminapi.benefit.entity.enums.BenefitPolicyCode;
import com.itplace.adminapi.benefit.entity.enums.MainCategory;
import com.itplace.adminapi.benefit.exception.BenefitNotFoundException;
import com.itplace.adminapi.benefit.exception.InvalidBenefitPolicyException;
import com.itplace.adminapi.benefit.exception.NoBenefitUpdateException;
import com.itplace.adminapi.benefit.repository.BenefitPolicyRepository;
import com.itplace.adminapi.benefit.repository.BenefitRepository;
import com.itplace.adminapi.favorite.repository.FavoriteRepository;
import com.itplace.adminapi.history.repository.MembershipHistoryRepository;
import com.itplace.adminapi.log.dto.RankResult;
import com.itplace.adminapi.log.repository.LogRepository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BenefitServiceImpl implements BenefitService {

    private final FavoriteRepository favoriteRepository;
    private final BenefitRepository benefitRepository;
    private final LogRepository logRepository;
    private final MembershipHistoryRepository membershipHistoryRepository;
    private final BenefitPolicyRepository benefitPolicyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteRankResponse> favoriteRank(int limit) {
        return favoriteRepository.findTopFavoriteRank(limit).stream()
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
    @Transactional(readOnly = true)
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
                .benefitLimit(benefit.getBenefitPolicy().getName())
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
            BenefitPolicyCode policyCode = mapKoreanNameToCode(request.getBenefitLimit());

            BenefitPolicy newPolicy = benefitPolicyRepository.findByCode(policyCode)
                    .orElseThrow(() -> new InvalidBenefitPolicyException(BenefitCode.BENEFIT_POLICY_NOT_FOUND));

            if (!newPolicy.equals(benefit.getBenefitPolicy())) {
                benefit.setBenefitPolicy(newPolicy);
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
    @Transactional(readOnly = true)
    public Long getBenefitCount() {
        return benefitRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<BenefitResponse> getBenefitList(BenefitListRequest request) {
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 8;
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "id";
        Sort.Direction direction = Sort.Direction.fromString(request.getDirection() != null ? request.getDirection() : "asc");
        MainCategory mainCategory = request.getMainCategory() != null ? request.getMainCategory() : MainCategory.BASIC_BENEFIT;

        Map<Long, Integer> searchRankMap = getRankMap(logRepository.findSearchRank());
        Map<Long, Integer> favoriteRankMap = mainCategory == MainCategory.BASIC_BENEFIT ?
                getRankMap(favoriteRepository.findBasicFavoriteRank()) : getRankMap(favoriteRepository.findVipFavoriteRank());;
        Map<Long, Integer> usageRankMap = mainCategory == MainCategory.BASIC_BENEFIT ?
                getRankMap(membershipHistoryRepository.findBasicHistoryRank()) : getRankMap(membershipHistoryRepository.findVipHistoryRank());

        List<Benefit> benefits = benefitRepository.findAllByMainCategory(mainCategory);

        List<Benefit> filtered = benefits.stream()
                .filter(b -> request.getCategory() == null || request.getCategory().isEmpty() ||
                        b.getPartner().getCategory().trim().equalsIgnoreCase(request.getCategory()))
                .filter(b ->request.getType() == null || b.getType().equals(request.getType()))
                .filter(b -> request.getKeyword() == null || request.getKeyword().isEmpty() ||
                        b.getBenefitName().toLowerCase().trim().contains(request.getKeyword().toLowerCase()))
                .toList();

        List<Benefit> sortBenefits = filtered.stream()
                .sorted(getComparator(sortBy, searchRankMap, favoriteRankMap, usageRankMap, direction))
                .skip((long) page * size)
                .limit(size)
                .toList();

        List<BenefitResponse> content = sortBenefits.stream()
                .map(b -> BenefitResponse.builder()
                        .benefitId(b.getBenefitId())
                        .benefitName(b.getBenefitName())
                        .mainCategory(b.getMainCategory())
                        .category(b.getPartner().getCategory())
                        .type(b.getType())
                        .image(b.getPartner().getImage())
                        .searchRank(searchRankMap.getOrDefault(b.getBenefitId(),benefits.size()).longValue())
                        .favoriteRank(favoriteRankMap.getOrDefault(b.getBenefitId(),benefits.size()).longValue())
                        .usageRank(usageRankMap.getOrDefault(b.getBenefitId(),benefits.size()).longValue())
                        .build()
                ).toList();
        long totalElements = filtered.size();
        int totalPages = (int) Math.ceil(totalElements / (double) size);
        boolean last = (page > totalPages - 1);
        return PagedResponse.<BenefitResponse>builder()
                .content(content)
                .currentPage(page)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .hasNext(last)
                .build();
    }

    private Map<Long, Integer> getRankMap(List<RankResult> rankList) {
        Map<Long, Integer> map = new HashMap<>();

        int rank = 0;
        int prevCount = -1;
        int sameRankCount = 0;
        for (RankResult rankResult : rankList) {
            int count = (int) rankResult.getCount();

            if (count != prevCount) {
                rank = rank + 1 + sameRankCount;
                sameRankCount = 0;
                prevCount = count;
            } else {
                sameRankCount++;
            }

            map.put(rankResult.getId(), rank);
        }
        return map;
    }

    private Comparator<Benefit> getComparator(
            String sortBy, Map<Long, Integer> searchRankMap, Map<Long, Integer> favoriteMap,
            Map<Long, Integer> usageRankMap, Sort.Direction direction) {

        Comparator<Benefit> comparator = switch (sortBy.toLowerCase()) {
            case "search" -> Comparator.comparingInt(b ->
                    searchRankMap.getOrDefault(b.getBenefitId(), Integer.MAX_VALUE));
            case "favorite" -> Comparator.comparingInt(b ->
                    favoriteMap.getOrDefault(b.getBenefitId(), Integer.MAX_VALUE));
            case "usage" -> Comparator.comparingInt(b ->
                    usageRankMap.getOrDefault(b.getBenefitId(), Integer.MAX_VALUE));
            default -> Comparator.comparingLong(Benefit::getBenefitId);
        };

        if(direction == Direction.DESC){
            comparator = comparator.reversed();
        }

        comparator = comparator.thenComparingLong(Benefit::getBenefitId);

        return comparator;
    }

    private BenefitPolicyCode mapKoreanNameToCode(String name) {
        // 모든 공백 제거 후 비교 (정규화)
        String normalized = name.replaceAll("\\s+", ""); // "월 1 회" -> "월1회"

        switch (normalized) {
            case "월1회":
                return BenefitPolicyCode.MONTHLY_ONCE;
            case "일1회":
                return BenefitPolicyCode.DAILY_ONCE;
            case "제한없음":
                return BenefitPolicyCode.UNLIMITED;
            case "최초1회":
                return BenefitPolicyCode.ONCE;
            default:
                throw new InvalidBenefitPolicyException(BenefitCode.INVALID_BENEFIT_POLICY);
        }
    }
}
