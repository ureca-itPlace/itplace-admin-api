package com.itplace.adminapi.log.dto;

import lombok.Data;

@Data
public class SearchRankResult {
    private long partnerId;
    private long searchCount;
    private long rank;
}
