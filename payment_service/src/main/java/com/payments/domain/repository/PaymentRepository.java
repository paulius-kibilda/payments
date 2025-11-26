package com.payments.domain.repository;

import com.payments.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("""
            SELECT p FROM Payment p
            WHERE (:onlyActive IS NULL OR p.canceled <> :onlyActive)
              AND (:minAmount IS NULL OR p.amount >= :minAmount)
              AND (:maxAmount IS NULL OR p.amount <= :maxAmount)
            """)
    List<Payment> findForQuery(Boolean onlyActive, BigDecimal minAmount, BigDecimal maxAmount);
}
