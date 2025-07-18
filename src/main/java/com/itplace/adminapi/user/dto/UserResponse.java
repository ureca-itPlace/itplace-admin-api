package com.itplace.adminapi.user.dto;

import com.itplace.adminapi.benefit.entity.enums.Grade;
import com.itplace.adminapi.user.entity.UserType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate birthday;
    private Grade grade;
    private UserType userType;
}
