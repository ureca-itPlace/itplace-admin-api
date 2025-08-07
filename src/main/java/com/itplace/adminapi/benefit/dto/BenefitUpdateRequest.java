package com.itplace.adminapi.benefit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenefitUpdateRequest {
    private String benefitLimit;
    private String manual;
}
