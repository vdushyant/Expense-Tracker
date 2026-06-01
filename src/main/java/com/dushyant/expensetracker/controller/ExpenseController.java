package com.dushyant.expensetracker.controller;

import com.dushyant.expensetracker.dto.ExpenseRequest;
import com.dushyant.expensetracker.dto.ExpenseResponse;
import com.dushyant.expensetracker.service.ExpenseService;
import com.dushyant.expensetracker.service.ExpenseServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ExpenseResponse addExpense(@Valid @RequestBody ExpenseRequest request){
        return expenseService.addExpense(request);
    }

    @GetMapping
    public List<ExpenseResponse> getAllExpenses(){
        return expenseService.getAllExpense();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportExpensesToCsv() {
        byte[] csvData = expenseService.exportExpensesToCsv();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expenses.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }

    @GetMapping("/{id}")
    public ExpenseResponse getExpenseById(@PathVariable Long id) {
        return expenseService.getExpenseById(id);
    }

    @PutMapping("/{id}")
    public ExpenseResponse updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request
    ) {
        return expenseService.updateExpense(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return "Expense deleted successfully";
    }

    @GetMapping("/category/{category}")
    public List<ExpenseResponse> getExpensesByCategory(@PathVariable String category) {
        return expenseService.getExpensesByCategory(category);
    }

    @GetMapping("/summary")
    public String getTotalSummary() {
        return expenseService.getTotalSummary();
    }

    @GetMapping("/summary/month/{month}")
    public String getMonthlySummary(@PathVariable int month) {
        return expenseService.getMonthlySummary(month);
    }
}
