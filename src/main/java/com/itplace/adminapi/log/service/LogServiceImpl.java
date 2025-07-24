package com.itplace.adminapi.log.service;

import com.itplace.adminapi.benefit.entity.Benefit;
import com.itplace.adminapi.benefit.repository.BenefitRepository;
import com.itplace.adminapi.log.dto.ClickRankResponse;
import com.itplace.adminapi.log.dto.SearchRankResponse;
import com.itplace.adminapi.log.dto.RankResult;
import com.itplace.adminapi.log.repository.LogRepository;
import com.itplace.adminapi.partner.entity.Partner;
import com.itplace.adminapi.partner.repository.PartnerRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService{

    private final LogRepository logRepository;
    private final BenefitRepository benefitRepository;
    private final PartnerRepository partnerRepository;

    @Override
    public List<ClickRankResponse> clickRank(int limit) {
        Instant from = Instant.now().minus(Duration.ofDays(300));
        List<RankResult> topClicks = logRepository.findTopClickRank(from, limit);

        return topClicks.stream()
                .map(click -> {
                    Optional<Benefit> benefitOptional = benefitRepository.findByBenefitId(click.getId());
                    System.out.println("benefitId: " + click.getId());
                    String partnerName = null;
                    if(benefitOptional.isPresent()){
                        Benefit benefit = benefitOptional.get();
                        partnerName = benefit.getPartner().getPartnerName();
                    }
                    return new ClickRankResponse(
                            partnerName, click.getCount()
                    );
                })
                .toList();
    }

    @Override
    public List<SearchRankResponse> searchRank(int recentDay, int prevDay, int limit) {
        Instant now = Instant.now();
        Instant from = now.minus(Duration.ofDays(recentDay));
        Instant prevFrom = from.minus(Duration.ofDays(prevDay));
        Instant prevTo = from;

        List<RankResult> recentRanks = logRepository.findTopSearchRank(from, now, limit);
        List<RankResult> prevRanks = logRepository.findTopSearchRank(prevFrom, prevTo, limit);

        Map<Long, Long> prevRankMap = new HashMap<>();
        AtomicLong prevCount = new AtomicLong(1);
        prevRanks.stream().sorted(Comparator.comparing(RankResult::getCount).reversed())
                .forEach(searchRank ->
                        prevRankMap.put(searchRank.getId(), prevCount.getAndIncrement()));

        AtomicLong rankCount = new AtomicLong(1);

        return recentRanks.stream().map(r -> {
            Optional<Partner> partnerOpt = partnerRepository.findById(r.getId());
            String partnerName = null;
            if(partnerOpt.isPresent()){
                Partner  partner = partnerOpt.get();
                partnerName = partner.getPartnerName();
            }
            long rank = rankCount.getAndIncrement();

            Long prevRank = prevRankMap.getOrDefault(r.getId(), 0L);
            System.out.println("prevRank: " + prevRank);

            long rankChange = prevRank == 0 ? 0 : prevRank - rank;

            String changeDirection;
            if(prevRank == 0L) changeDirection = "NEW";
            else if (rankChange > 0) changeDirection = "UP";
            else if (rankChange < 0) changeDirection = "DOWN";
            else changeDirection = "SAME";

            return new SearchRankResponse(
                    partnerName,
                    r.getCount(),
                    rank,
                    prevRank == 0L ? 99999 : prevRank,
                    rankChange,
                    changeDirection);
        }).toList();
    }
}
