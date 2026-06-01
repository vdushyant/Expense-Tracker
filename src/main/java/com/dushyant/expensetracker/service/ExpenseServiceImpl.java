package com.dushyant.expensetracker.service;

import com.dushyant.expensetracker.dto.ExpenseRequest;
import com.dushyant.expensetracker.dto.ExpenseResponse;
import com.dushyant.expensetracker.entity.Expense;
import com.dushyant.expensetracker.exception.ResourceNotFoundException;
import com.dushyant.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService{
    private final ExpenseRepository expenseRepository;

    @Override
    public ExpenseResponse addExpense(ExpenseRequest request){
        Expense expense = Expense.builder()
                .description(request.getDescription())
                .amount(request.getAmount())
                .category(request.getCategory())
                .expenseDate(request.getExpense())
                .build();

        Expense savedExpense = expenseRepository.save(expense);
        return mapToResponse(savedExpense);
    }

    @Override
    public List<ExpenseResponse> getAllExpense(){
        return expenseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ExpenseResponse getExpenseById(Long id){
        Expense expense = expenseRepository.findById(id).orElseThrow(()
                ->new ResourceNotFoundException("Expense not found with id: "+id));

        return mapToResponse(expense);
    }

    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request){
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Expense not found with id: "+id));

        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpense());

        Expense updatedExpense  = expenseRepository.save(expense);
        return mapToResponse(updatedExpense);
    }

    @Override
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        expenseRepository.delete(expense);
    }

    @Override
    public List<ExpenseResponse> getExpensesByCategory(String category) {
        return expenseRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public String getTotalSummary() {
        BigDecimal total = expenseRepository.findAll()
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return "Total expenses: ₹" + total;
    }

    @Override
    public String getMonthlySummary(int month) {
        int currentYear = LocalDate.now().getYear();

        YearMonth yearMonth = YearMonth.of(currentYear, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        BigDecimal total = expenseRepository.findByExpenseDateBetween(startDate, endDate)
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return "Total expenses for month " + month + ": ₹" + total;
    }

    @Override
    public byte[] exportExpensesToCsv() {
        List<Expense> expenses = expenseRepository.findAll();

        StringBuilder csv = new StringBuilder();

        csv.append("ID,Description,Amount,Category,Expense Date\n");

        for (Expense expense : expenses) {
            csv.append(expense.getId()).append(",");
            csv.append(escapeCsv(expense.getDescription())).append(",");
            csv.append(expense.getAmount()).append(",");
            csv.append(escapeCsv(expense.getCategory())).append(",");
            csv.append(expense.getExpenseDate()).append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");

        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    }


    private ExpenseResponse mapToResponse(Expense expense){
        return ExpenseResponse.builder()
                .id(expense.getId())
                .description(expense.getDescription())
                .amount(expense.getAmount())
                .category(expense.getCategory())
                .expenseDate(expense.getExpenseDate())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }
}
