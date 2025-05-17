package org.example.thith.repository;

import org.example.thith.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByDiscountAmount(BigDecimal discountAmount);
    List<Promotion> findByStartDate(LocalDate startDate);
    List<Promotion> findByEndDate(LocalDate endDate);
    List<Promotion> findByDiscountAmountAndStartDateAndEndDate(
            BigDecimal discountAmount, LocalDate startDate, LocalDate endDate);



}