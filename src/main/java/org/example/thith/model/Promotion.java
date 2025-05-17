package org.example.thith.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "promotion")
@Data
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tiêu đề là bắt buộc")
    private String title;

    @NotNull(message = "Thời gian bắt đầu là bắt buộc")
    @Future(message = "Thời gian bắt đầu phải sau ngày hiện tại")
    private LocalDate startDate;

    @NotNull(message = "Thời gian kết thúc là bắt buộc")
    private LocalDate endDate;

    @NotNull(message = "Mức giảm giá là bắt buộc")
    @DecimalMin(value = "10000.00", message = "Mức giảm giá phải ít nhất 10.000 VNĐ")
    private BigDecimal discountAmount;

    private String details;
    public boolean isValidDateRange() {
        return endDate != null && startDate != null &&
                endDate.isAfter(startDate.plusDays(1));
    }
}