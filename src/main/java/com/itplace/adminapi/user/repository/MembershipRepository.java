package com.itplace.adminapi.user.repository;

import com.itplace.adminapi.user.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, String> {
}
