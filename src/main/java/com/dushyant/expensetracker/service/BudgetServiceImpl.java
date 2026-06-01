package com.dushyant.expensetracker.service;
import com.dushyant.expensetracker.dto.BudgetRequest;
import com.dushyant.expensetracker.dto.BudgetResponse;
import com.dushyant.expensetracker.entity.Budget;
import com.dushyant.expensetracker.entity.Expense;
import com.dushyant.expensetracker.exception.ResourceNotFoundException;
import com.dushyant.expensetracker.repository.BudgetRepository;
import com.dushyant.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    public BudgetResponse setBudget(BudgetRequest request) {
        Integer currentYear = LocalDate.now().getYear();

        Budget budget = budgetRepository.findByMonthAndYear(request.getMonth(), currentYear)
                .orElse(Budget.builder()
                        .month(request.getMonth())
                        .year(currentYear)
                        .build());

        budget.setAmount(request.getAmount());

        Budget savedBudget = budgetRepository.save(budget);

        return mapToResponse(savedBudget);
    }

    @Override
    public BudgetResponse getBudgetByMonth(Integer month) {
        Integer currentYear = LocalDate.now().getYear();

        Budget budget = budgetRepository.findByMonthAndYear(month, currentYear)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found for month: " + month));

        return mapToResponse(budget);
    }

    @Override
    public String getBudgetStatus(Integer month) {
        Integer currentYear = LocalDate.now().getYear();

        Budget budget = budgetRepository.findByMonthAndYear(month, currentYear)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found for month: " + month));

        YearMonth yearMonth = YearMonth.of(currentYear, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        BigDecimal totalExpense = expenseRepository.findByExpenseDateBetween(startDate, endDate)
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalExpense.compareTo(budget.getAmount()) > 0) {
            return "Warning: Budget exceeded. Budget: ₹" + budget.getAmount()
                    + ", Expense: ₹" + totalExpense;
        }

        BigDecimal remaining = budget.getAmount().subtract(totalExpense);

        return "Budget is safe. Budget: ₹" + budget.getAmount()
                + ", Expense: ₹" + totalExpense
                + ", Remaining: ₹" + remaining;
    }

    private BudgetResponse mapToResponse(Budget budget) {
        return BudgetResponse.builder()
                .id(budget.getId())
                .month(budget.getMonth())
                .year(budget.getYear())
                .amount(budget.getAmount())
                .build();
    }
}