package com.itplace.adminapi.history.repository;

import com.itplace.adminapi.history.entity.MembershipHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipHistoryRepository extends JpaRepository<MembershipHistory, Long> {
    List<MembershipHistory> findByMembership_MembershipIdOrderByUsedAtDesc(String membershipId);
}
