package com.itplace.adminapi.log.controller;

import com.itplace.adminapi.common.ApiResponse;
import com.itplace.adminapi.log.LogCode;
import com.itplace.adminapi.log.dto.ClickRankResponse;
import com.itplace.adminapi.log.dto.SearchRankResponse;
import com.itplace.adminapi.log.service.LogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping("/benefits/most-clicked")
    public ResponseEntity<ApiResponse<?>> getMostClicked(@RequestParam(defaultValue = "5") int limit) {
        List<ClickRankResponse> clickRank = logService.clickRank(limit);
        ApiResponse<?> body = ApiResponse.of(LogCode.BENEFITS_MOST_CLICKED_SUCCESS, clickRank);

        return ResponseEntity.status(body.getStatus()).body(body);
    }

    @GetMapping("/partners/search-ranking")
    public ResponseEntity<ApiResponse<?>> getPartnerRanking(
            @RequestParam(defaultValue = "2") int recentDay,
            @RequestParam(defaultValue = "3") int prevDay,
            @RequestParam(defaultValue = "5") int limit) {
        List<SearchRankResponse> searchRank = logService.searchRank(recentDay, prevDay, limit);
        ApiResponse<?> body = ApiResponse.of(LogCode.PARTNERS_SEARCH_RANKING_SUCCESS, searchRank);

        return ResponseEntity.status(body.getStatus()).body(body);
    }
}
