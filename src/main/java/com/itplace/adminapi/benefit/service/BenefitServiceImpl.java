package com.itplace.adminapi.benefit.service;

import com.itplace.adminapi.benefit.dto.FavoriteRankResponse;
import com.itplace.adminapi.favorite.repository.FavoriteRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BenefitServiceImpl implements BenefitService {

    private final FavoriteRepository favoriteRepository;


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
}
