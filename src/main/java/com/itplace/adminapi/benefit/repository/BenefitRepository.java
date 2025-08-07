package com.itplace.adminapi.benefit.repository;

import com.itplace.adminapi.benefit.entity.Benefit;
import com.itplace.adminapi.benefit.entity.enums.MainCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BenefitRepository extends JpaRepository<Benefit, Long> {

    @Query("""
    SELECT b
    FROM Benefit b
    JOIN FETCH b.partner p
    LEFT JOIN FETCH b.tierBenefits tb
    WHERE b.benefitId = :benefitId
""")
    Optional<Benefit> findBenefitWithPartnerById(@Param("benefitId") Long benefitId);

    Optional<Benefit> findByBenefitId(Long benefitId);

    List<Benefit> findAllByMainCategory(MainCategory mainCategory);
}
