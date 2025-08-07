package com.itplace.adminapi.user.controller;

import com.itplace.adminapi.benefit.entity.enums.Grade;
import com.itplace.adminapi.common.ApiResponse;
import com.itplace.adminapi.user.UserCode;
import com.itplace.adminapi.user.dto.PagedResponse;
import com.itplace.adminapi.user.dto.UserResponse;
import com.itplace.adminapi.user.entity.UserType;
import com.itplace.adminapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<?>> getTotalUserCount() {
        long total = userService.totalUsers();
        ApiResponse<?> body = ApiResponse.of(UserCode.USER_TOTAL_COUNT_SUCCESS, total);

        return ResponseEntity.status(body.getStatus()).body(body);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUsers(
            @RequestParam(value = "userType", required = false) UserType userType,
            @RequestParam(value = "grade", required = false) Grade grade,
            @PageableDefault(page = 0, size = 8) Pageable pageable) {
        PagedResponse<UserResponse> userDtoPage = userService.getUserList(userType, grade, pageable);

        ApiResponse<?> body = ApiResponse.of(UserCode.USER_PAGE_SUCCESS, userDtoPage);

        return ResponseEntity.status(body.getStatus()).body(body);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchUsers(
            @RequestParam(value = "keyword") String keyword,
            @PageableDefault(page = 0, size = 8) Pageable pageable) {
        PagedResponse<UserResponse> userDtoPage = userService.findbyKeyword(keyword, pageable);

        ApiResponse<?> body = ApiResponse.of(UserCode.USER_SEARCH_SUCCESS, userDtoPage);

        return ResponseEntity.status(body.getStatus()).body(body);
    }
}
