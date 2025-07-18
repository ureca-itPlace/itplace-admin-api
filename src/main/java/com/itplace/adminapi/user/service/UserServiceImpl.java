package com.itplace.adminapi.user.service;

import com.itplace.adminapi.benefit.entity.enums.Grade;
import com.itplace.adminapi.user.UserCode;
import com.itplace.adminapi.user.dto.PagedResponse;
import com.itplace.adminapi.user.dto.UserResponse;
import com.itplace.adminapi.user.entity.Membership;
import com.itplace.adminapi.user.entity.User;
import com.itplace.adminapi.user.entity.UserType;
import com.itplace.adminapi.user.exception.UserKeywordException;
import com.itplace.adminapi.user.repository.MembershipRepository;
import com.itplace.adminapi.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    @Override
    public long totalUsers(){
        return userRepository.count();
    }

    @Override
    public PagedResponse<UserResponse> getUserList(UserType userType, Grade grade, Pageable pageable) {

        Boolean hasMembershipId = null;
        if(userType != null) {
            if(userType == UserType.LINKED){
                hasMembershipId = true;
            } else if (userType == UserType.STANDARD) {
                hasMembershipId = false;
            }
        }

        Page<User> userPage = userRepository.filterByUserTypeAndGrade(
                hasMembershipId, grade, pageable
        );

        Page<UserResponse> dtoPage = userPage.map(user -> {
            Grade actualGrade = null;
            if(user.getMembershipId() != null) {
                Membership membership = membershipRepository.findById(user.getMembershipId()).orElse(null);
                if(membership != null) actualGrade = membership.getGrade();
            }
            return UserResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .birthday(user.getBirthday())
                    .grade(actualGrade)
                    .userType(actualGrade != null ? UserType.LINKED : UserType.STANDARD)
                    .build();
        });
        return new PagedResponse<>(
                dtoPage.getContent(),
                dtoPage.getNumber(),
                dtoPage.getTotalPages(),
                dtoPage.getTotalElements(),
                dtoPage.hasNext()
        );
    }

    @Override
    public PagedResponse<UserResponse> findbyKeyword(String keyword, Pageable pageable){
        if(keyword == null || keyword.isBlank()) {
            throw new UserKeywordException(UserCode.KEYWORD_REQUIRED);
        }
        Page<User> userPage = userRepository.searchByKeyword(keyword, pageable);

        Page<UserResponse> dtoPage = userPage.map(user -> {
            Grade actualGrade = null;
            if(user.getMembershipId() != null) {
                Membership membership = membershipRepository.findById(user.getMembershipId()).orElse(null);
                if(membership != null) actualGrade = membership.getGrade();
            }
            return UserResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .birthday(user.getBirthday())
                    .grade(actualGrade)
                    .userType(actualGrade != null ? UserType.LINKED : UserType.STANDARD)
                    .build();
        });
        return new PagedResponse<>(
                dtoPage.getContent(),
                dtoPage.getNumber(),
                dtoPage.getTotalPages(),
                dtoPage.getTotalElements(),
                dtoPage.hasNext()
        );
    }
}
