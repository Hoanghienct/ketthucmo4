package org.example.thith.service;

import org.example.thith.model.Promotion;
import org.example.thith.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    private PromotionRepository repository;

    @Override
    public List<Promotion> findAll() {
        return repository.findAll();
    }

    @Override
    public Promotion findById(Long id) {
        Optional<Promotion> promotion = repository.findById(id);
        return promotion.orElse(null);
    }

    @Override
    public Promotion save(Promotion promotion) {
        if (!promotion.isValidDateRange()) {
            throw new IllegalArgumentException("End date must be at least one day after start date");
        }
        return repository.save(promotion);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Promotion> search(BigDecimal discountAmount, LocalDate startDate, LocalDate endDate) {
        if (discountAmount != null && startDate != null && endDate != null) {
            return repository.findByDiscountAmountAndStartDateAndEndDate(discountAmount, startDate, endDate);
        } else if (discountAmount != null) {
            return repository.findByDiscountAmount(discountAmount);
        } else if (startDate != null) {
            return repository.findByStartDate(startDate);
        } else if (endDate != null) {
            return repository.findByEndDate(endDate);
        }
        return findAll();
    }
}
