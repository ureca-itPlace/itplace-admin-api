package com.itplace.adminapi.benefit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoriteRankResponse {
    private Long benefitId;
    private String partnerName;
    private String mainCategory;
    private Long favoriteCount;
    private Long rank;
}
