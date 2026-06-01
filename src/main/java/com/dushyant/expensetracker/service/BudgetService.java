package com.dushyant.expensetracker.service;
import com.dushyant.expensetracker.dto.BudgetRequest;
import com.dushyant.expensetracker.dto.BudgetResponse;

public interface BudgetService {

    BudgetResponse setBudget(BudgetRequest request);

    BudgetResponse getBudgetByMonth(Integer month);

    String getBudgetStatus(Integer month);
}