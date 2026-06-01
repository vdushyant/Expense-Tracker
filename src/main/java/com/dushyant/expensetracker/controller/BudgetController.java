package com.dushyant.expensetracker.controller;
import com.dushyant.expensetracker.dto.BudgetRequest;
import com.dushyant.expensetracker.dto.BudgetResponse;
import com.dushyant.expensetracker.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public BudgetResponse setBudget(@Valid @RequestBody BudgetRequest request) {
        return budgetService.setBudget(request);
    }

    @GetMapping("/month/{month}")
    public BudgetResponse getBudgetByMonth(@PathVariable Integer month) {
        return budgetService.getBudgetByMonth(month);
    }

    @GetMapping("/month/{month}/status")
    public String getBudgetStatus(@PathVariable Integer month) {
        return budgetService.getBudgetStatus(month);
    }
}