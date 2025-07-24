package com.itplace.adminapi.history.repository;

import com.itplace.adminapi.history.entity.MembershipHistory;
import com.itplace.adminapi.log.dto.RankResult;
import com.itplace.adminapi.partner.dto.UsageRankProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MembershipHistoryRepository extends JpaRepository<MembershipHistory, Long> {

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

    @Query(value = """
                SELECT mh.benefitId AS Id, COUNT(*) AS count
                FROM membershipHistory mh
                JOIN Benefit b ON mh.benefitId = b.benefitId
                WHERE b.mainCategory = "기본 혜택"
                GROUP BY mh.benefitId
                ORDER BY count desc
            """, nativeQuery = true)
    List<RankResult> findBasicHistoryRank();

    @Query(value = """
                SELECT mh.benefitId AS Id, COUNT(*) AS count
                FROM membershipHistory mh
                JOIN Benefit b ON mh.benefitId = b.benefitId
                WHERE b.mainCategory = "VIP 콕"
                GROUP BY mh.benefitId
                ORDER BY count desc
            """, nativeQuery = true)
    List<RankResult> findVipHistoryRank();
}
