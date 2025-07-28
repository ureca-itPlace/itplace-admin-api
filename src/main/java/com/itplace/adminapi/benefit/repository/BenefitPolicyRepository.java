package com.itplace.adminapi.benefit.repository;

import com.itplace.adminapi.benefit.entity.BenefitPolicy;
import com.itplace.adminapi.benefit.entity.enums.BenefitPolicyCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenefitPolicyRepository extends JpaRepository<BenefitPolicy, Long> {
    Optional<BenefitPolicy> findByCode(BenefitPolicyCode code);
}
