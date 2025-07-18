package com.itplace.adminapi.user.repository;

import com.itplace.adminapi.benefit.entity.enums.Grade;
import com.itplace.adminapi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
    SELECT u FROM User u LEFT JOIN Membership m ON u.membershipId = m.membershipId
        WHERE (:hasMembershipId IS NULL OR (:hasMembershipId = true AND u.membershipId IS NOT NULL)
            OR (:hasMembershipId = false AND u.membershipId IS NULL))
        AND (:grade IS NULL OR m.grade = :grade)
    """)
    Page<User> filterByUserTypeAndGrade(
            @Param("hasMembershipId") Boolean hasMembershipId,
            @Param("grade") Grade grade, Pageable pageable);

    @Query("""
    SELECT u FROM User u WHERE u.name LIKE %:keyword% OR u.email LIKE %:keyword%
    """)
    Page<User> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
