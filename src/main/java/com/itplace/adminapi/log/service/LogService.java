package com.itplace.adminapi.log.service;

import com.itplace.adminapi.log.dto.ClickRankResponse;
import com.itplace.adminapi.log.dto.SearchRankResponse;
import java.util.List;

public interface LogService {
    List<ClickRankResponse> clickRank(int limit);
    List<SearchRankResponse> searchRank(int recentDay, int prevDay, int limit);
}
