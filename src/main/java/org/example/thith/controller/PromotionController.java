package org.example.thith.controller;

import jakarta.validation.Valid;
import org.example.thith.model.Promotion;
import org.example.thith.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/promotions")
public class PromotionController {
    @Autowired
    private PromotionService service;
    @GetMapping
    public String listPromotions(Model model) {
        try {
            model.addAttribute("promotions", service.findAll());
            model.addAttribute("searchPromotion", new Promotion());
            return "promotion/list";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Lỗi khi tải danh sách khuyến mãi: " + e.getMessage());
            return "promotion/list";
        }
    }
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "promotion/add";
    }

    @PostMapping("/add")
    public String addPromotion(@Valid @ModelAttribute("promotion") Promotion promotion,
                               BindingResult result, Model model) {
        if (result.hasErrors() || !promotion.isValidDateRange()) {
            if (!promotion.isValidDateRange()) {
                result.rejectValue("endDate", "error.promotion",
                        "Thời gian kết thúc phải sau thời gian bắt đầu ít nhất 1 ngày");
            }
            return "promotion/add";
        }
        service.save(promotion);
        return "redirect:/promotions?success=" + URLEncoder.encode("Khuyến mãi đã được thêm thành công", StandardCharsets.UTF_8);
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Promotion promotion = service.findById(id);
        if (promotion == null) {
            return "redirect:/promotions?error=" + URLEncoder.encode("Không tìm thấy khuyến mãi", StandardCharsets.UTF_8);
        }
        model.addAttribute("promotion", promotion);
        return "promotion/edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePromotion(@PathVariable Long id,
                                  @Valid @ModelAttribute("promotion") Promotion promotion,
                                  BindingResult result, Model model) {
        if (result.hasErrors() || !promotion.isValidDateRange()) {
            if (!promotion.isValidDateRange()) {
                result.rejectValue("endDate", "error.promotion",
                        "Thời gian kết thúc phải sau thời gian bắt đầu ít nhất 1 ngày");
            }
            return "promotion/edit";
        }
        promotion.setId(id);
        service.save(promotion);
        return "redirect:/promotions?success=" + URLEncoder.encode("Khuyến mãi đã được cập nhật thành công", StandardCharsets.UTF_8);
    }

    @GetMapping("/delete/{id}")
    public String deletePromotion(@PathVariable Long id) {
        Promotion promotion = service.findById(id);
        if (promotion != null) {
            service.deleteById(id);
            return "redirect:/promotions?success=" + URLEncoder.encode("Khuyến mãi đã được xóa thành công", StandardCharsets.UTF_8);
        }
        return "redirect:/promotions?error=" + URLEncoder.encode("Không tìm thấy khuyến mãi để xóa", StandardCharsets.UTF_8);
    }

    @GetMapping("/search")
    public String searchPromotions(
            @RequestParam(value = "discountAmount", required = false) String discountAmount,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            Model model) {
        BigDecimal discount = null;
        try {
            if (discountAmount != null && !discountAmount.trim().isEmpty()) {
                discount = new BigDecimal(discountAmount.trim());
            }
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Mức giảm giá phải là một số hợp lệ (ví dụ: 50000)");
            model.addAttribute("promotions", service.findAll());
            model.addAttribute("searchPromotion", new Promotion());
            return "promotion/list";
        }

        List<Promotion> promotions = service.search(discount, startDate, endDate);
        model.addAttribute("promotions", promotions);
        model.addAttribute("searchPromotion", new Promotion());
        if (promotions.isEmpty() && (startDate != null || endDate != null)) {
            model.addAttribute("info", "Không tìm thấy khuyến mãi theo ngày đã chọn.");
        }
        return "promotion/list";
    }
}