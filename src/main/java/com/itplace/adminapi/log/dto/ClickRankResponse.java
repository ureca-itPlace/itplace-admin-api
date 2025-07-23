package com.itplace.adminapi.log.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClickRankResponse {
    private String partnerName;
    private Long clickCount;
}
