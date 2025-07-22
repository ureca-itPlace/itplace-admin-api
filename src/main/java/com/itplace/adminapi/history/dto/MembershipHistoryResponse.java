package com.itplace.adminapi.history.dto;

import com.itplace.adminapi.benefit.entity.enums.Grade;
import java.util.List;
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
public class MembershipHistoryResponse {
    private Long userId;
    private String userName;
    private String membershipId;
    private Grade membershipGrade;
    private List<MembershipUsage> membershipUsage;
}
