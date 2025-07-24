package com.itplace.adminapi.benefit.dto;

import com.itplace.adminapi.benefit.entity.enums.BenefitType;
import com.itplace.adminapi.benefit.entity.enums.MainCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenefitResponse {

    private Long benefitId;
    private String benefitName;
    private MainCategory mainCategory;
    private String category;
    private BenefitType type;
    private String image;
    private Long searchRank;
    private Long favoriteRank;
    private Long usageRank;
}
