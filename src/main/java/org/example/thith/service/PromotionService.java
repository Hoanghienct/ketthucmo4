package org.example.thith.service;

import org.example.thith.model.Promotion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PromotionService {
    List<Promotion> findAll();
    Promotion findById(Long id);
    Promotion save(Promotion promotion);
    void deleteById(Long id);
    List<Promotion> search(BigDecimal discountAmount, LocalDate startDate, LocalDate endDate);
}