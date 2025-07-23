package com.itplace.adminapi.partner.repository;

import com.itplace.adminapi.partner.entity.Partner;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    Optional<Partner> findByPartnerId(Long partnerId);
}
