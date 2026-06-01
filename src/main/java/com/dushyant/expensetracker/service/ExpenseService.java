package com.dushyant.expensetracker.service;

import com.dushyant.expensetracker.dto.ExpenseRequest;
import com.dushyant.expensetracker.dto.ExpenseResponse;
import com.dushyant.expensetracker.entity.Expense;

import java.util.List;

public interface ExpenseService {
    ExpenseResponse addExpense(ExpenseRequest request);
    List<ExpenseResponse> getAllExpense();
    ExpenseResponse getExpenseById(Long id);
    ExpenseResponse updateExpense(Long id, ExpenseRequest expenseRequest);
    void deleteExpense(Long id);

    List<ExpenseResponse> getExpensesByCategory(String category);

    String getTotalSummary();

    String getMonthlySummary(int month);

    byte[] exportExpensesToCsv();
}
