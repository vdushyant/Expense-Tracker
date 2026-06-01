package com.dushyant.expensetracker.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ExpenseResponse {
    private Long id;
    private String description;
    private BigDecimal amount;
    private String category;
    private LocalDate expenseDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
