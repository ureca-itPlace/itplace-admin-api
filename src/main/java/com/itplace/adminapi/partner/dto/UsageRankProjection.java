package com.itplace.adminapi.partner.dto;

public interface UsageRankProjection {
    Long getPartnerId();
    String getPartnerName();
    Long getVvipUsageCount();
    Long getVipUsageCount();
    Long getBasicUsageCount();
    Long getTotalUsageCount();
}
