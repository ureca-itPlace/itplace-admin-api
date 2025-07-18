package com.itplace.adminapi.user.service;

import com.itplace.adminapi.benefit.entity.enums.Grade;
import com.itplace.adminapi.user.dto.PagedResponse;
import com.itplace.adminapi.user.dto.UserResponse;
import com.itplace.adminapi.user.entity.UserType;
import org.springframework.data.domain.Pageable;

public interface UserService {
    long totalUsers();
    PagedResponse<UserResponse> getUserList(UserType userType, Grade grade, Pageable pageable);
    PagedResponse<UserResponse> findbyKeyword(String keyword, Pageable pageable);

}
