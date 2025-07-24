package com.itplace.adminapi.benefit.controller;

import com.itplace.adminapi.benefit.BenefitCode;
import com.itplace.adminapi.benefit.dto.BenefitDetailResponse;
import com.itplace.adminapi.benefit.dto.BenefitListRequest;
import com.itplace.adminapi.benefit.dto.BenefitResponse;
import com.itplace.adminapi.benefit.dto.BenefitUpdateRequest;
import com.itplace.adminapi.benefit.dto.FavoriteRankResponse;
import com.itplace.adminapi.benefit.dto.PagedResponse;
import com.itplace.adminapi.benefit.service.BenefitService;
import com.itplace.adminapi.common.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/benefits")
@RequiredArgsConstructor
public class BenefitController {

    private final BenefitService benefitService;

    @GetMapping("/favorite")
    public ResponseEntity<ApiResponse<?>> getBenefitFavorite(@RequestParam(defaultValue = "5") int limit){
        List<FavoriteRankResponse> favoriteRank = benefitService.favoriteRank(limit);
        ApiResponse<?> body = ApiResponse.of(BenefitCode.BENEFIT_FAVORITE_SUCCESS, favoriteRank);

        return ResponseEntity.status(body.getStatus()).body(body);
    }

    @GetMapping("/{benefitId}")
    public ResponseEntity<ApiResponse<BenefitDetailResponse>> getBenefitDetail(@PathVariable Long benefitId) {
        BenefitDetailResponse result = benefitService.getBenefitDetail(benefitId);
        ApiResponse<BenefitDetailResponse> body = ApiResponse.of(BenefitCode.BENEFIT_DETAIL_SUCCESS, result);
        return ResponseEntity.status(body.getStatus()).body(body);
    }

    @PutMapping("/{benefitId}")
    public ResponseEntity<ApiResponse<Void>> updateBenefit(
            @PathVariable Long benefitId,
            @RequestBody BenefitUpdateRequest request
    ) {
        benefitService.updateBenefitInfo(benefitId, request);
        ApiResponse<Void> body = ApiResponse.ok(BenefitCode.BENEFIT_UPDATE_SUCCESS);
        return ResponseEntity.status(body.getStatus()).body(body);
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<Long>> getBenefitCount() {
        Long count = benefitService.getBenefitCount();
        ApiResponse<Long> body = ApiResponse.of(BenefitCode.BENEFIT_COUNT_SUCCESS, count);
        return ResponseEntity.status(body.getStatus()).body(body);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllBenefits(BenefitListRequest request) {
        PagedResponse<BenefitResponse> benefitList = benefitService.getBenefitList(request);
        ApiResponse<?> body = ApiResponse.of(BenefitCode.BENEFIT_LIST_SUCCESS, benefitList);

        return ResponseEntity.status(body.getStatus()).body(body);
    }
}
