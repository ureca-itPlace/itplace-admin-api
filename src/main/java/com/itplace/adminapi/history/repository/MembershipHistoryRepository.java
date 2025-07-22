package com.itplace.adminapi.history.repository;

import com.itplace.adminapi.history.entity.MembershipHistory;
import com.itplace.adminapi.partner.dto.UsageRankProjection;
import com.itplace.adminapi.partner.dto.UsageRankResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MembershipHistoryRepository extends JpaRepository<MembershipHistory, Long> {

    @Query("""
                SELECT mh
                FROM MembershipHistory mh
                JOIN FETCH mh.benefit b
                JOIN FETCH b.partner p
                WHERE mh.membership.membershipId = :membershipId
                  AND (:keyword IS NULL OR LOWER(b.benefitName) LIKE LOWER(CONCAT('%', :keyword, '%')))
                  AND (:startDate IS NULL OR mh.usedAt >= :startDate)
                  AND (:endDate IS NULL OR mh.usedAt <= :endDate)
                ORDER BY mh.usedAt DESC
            """)
    Page<MembershipHistory> findFiltered(
            @Param("membershipId") String membershipId,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("""
                SELECT COALESCE(SUM(mh.discountAmount), 0)
                FROM MembershipHistory mh
                WHERE mh.membership.membershipId = :membershipId
                  AND YEAR(mh.usedAt) = :year
                  AND MONTH(mh.usedAt) = :month
            """)
    Long sumDiscountAmountThisMonth(
            @Param("membershipId") String membershipId,
            @Param("year") int year,
            @Param("month") int month
    );

    @Query(value = """
                SELECT b.partnerId AS partnerId,
                p.partnerName AS partnerName,
                SUM(CASE WHEN m.grade = 'VVIP' THEN 1 ELSE 0 END) AS vvipUsageCount,
                SUM(CASE WHEN m.grade = 'VIP' THEN 1 ELSE 0 END) AS vipUsageCount,
                SUM(CASE WHEN m.grade = 'BASIC' THEN 1 ELSE 0 END) AS basicUsageCount,
                COUNT(*) AS totalUsageCount
                FROM MembershipHistory mh
                JOIN Benefit b ON mh.benefitId = b.benefitId
                JOIN Partner p ON b.partnerId = p.partnerId
                JOIN Membership m ON m.membershipId = mh.membershipId
                WHERE mh.usedAt >= DATE_SUB(NOW(), INTERVAL :days DAY)
                GROUP BY b.partnerId
                ORDER BY COUNT(mh.membershipId) DESC
                LIMIT 5;
            """, nativeQuery = true)
    List<UsageRankProjection> findTop5PartnerId(@Param("days") int days);

    List<MembershipHistory> findByMembership_MembershipIdOrderByUsedAtDesc(String membershipId);
}
