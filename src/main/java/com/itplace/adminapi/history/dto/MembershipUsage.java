package com.itplace.adminapi.history.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembershipUsage {
    private Long benefitId;
    private String benefitName;
    private LocalDate usedAt;
    private Long discountAmount;
}
