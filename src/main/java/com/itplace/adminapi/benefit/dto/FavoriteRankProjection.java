package com.itplace.adminapi.benefit.dto;

public interface FavoriteRankProjection {
    Long getBenefitId();
    String getPartnerName();
    String getMainCategory();
    Long getFavoriteCount();
    Long getFavoriteRank();
}
