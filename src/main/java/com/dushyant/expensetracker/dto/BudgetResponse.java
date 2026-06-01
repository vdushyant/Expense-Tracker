package com.dushyant.expensetracker.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BudgetResponse {

    private Long id;
    private Integer month;
    private Integer year;
    private BigDecimal amount;
}