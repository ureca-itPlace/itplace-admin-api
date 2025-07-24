package com.itplace.adminapi.benefit.dto;

import com.itplace.adminapi.benefit.entity.enums.BenefitType;
import com.itplace.adminapi.benefit.entity.enums.MainCategory;
import com.itplace.adminapi.benefit.entity.enums.UsageType;
import lombok.Data;

@Data
public class BenefitListRequest {
    private Integer page;        // default : 0
    private Integer size;        // default : 8
    private String sortBy;       // id, search, favorite, usage
    private String director;     // asc, desc
    private String keyword;
    private MainCategory mainCategory; // VIP_COCK, BASIC_BENEFIT
    private String category;
    private BenefitType type;
}
