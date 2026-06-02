package com.dushyant.expensetracker.service;

import com.dushyant.expensetracker.dto.ExpenseRequest;
import com.dushyant.expensetracker.dto.ExpenseResponse;
import com.dushyant.expensetracker.entity.Expense;
import com.dushyant.expensetracker.exception.ResourceNotFoundException;
import com.dushyant.expensetracker.repository.ExpenseRepository;
import com.dushyant.expensetracker.exception.BadRequestException;
import com.dushyant.expensetracker.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;

import com.dushyant.expensetracker.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService{
    private final ExpenseRepository expenseRepository;
    private final CurrentUserService currentUserService;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id",
            "description",
            "amount",
            "category",
            "expenseDate",
            "createdAt",
            "updatedAt"
    );

    @Override
    public ExpenseResponse addExpense(ExpenseRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        Expense expense = Expense.builder()
                .description(request.getDescription())
                .amount(request.getAmount())
                .category(request.getCategory())
                .expenseDate(request.getExpense())
                .user(currentUser)
                .build();

        Expense savedExpense = expenseRepository.save(expense);

        return mapToResponse(savedExpense);
    }

//    @Override
//    public List<ExpenseResponse> getAllExpense(){
//        return expenseRepository.findAll()
//                .stream()
//                .map(this::mapToResponse)
//                .toList();
//    }
@Override
public PagedResponse<ExpenseResponse> getAllExpenses(int page, int size, String sortBy, String direction) {

    if (page < 0) {
        throw new BadRequestException("Page number cannot be negative");
    }

    if (size < 1 || size > 100) {
        throw new BadRequestException("Page size must be between 1 and 100");
    }

    if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
        throw new BadRequestException("Invalid sort field: " + sortBy);
    }

    if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
        throw new BadRequestException("Sort direction must be either asc or desc");
    }

    Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);

    User currentUser = currentUserService.getCurrentUser();
    Page<Expense> expensePage = expenseRepository.findByUser(currentUser, pageable);

    List<ExpenseResponse> expenses = expensePage.getContent()
            .stream()
            .map(this::mapToResponse)
            .toList();

    return PagedResponse.<ExpenseResponse>builder()
            .content(expenses)
            .pageNumber(expensePage.getNumber())
            .pageSize(expensePage.getSize())
            .totalElements(expensePage.getTotalElements())
            .totalPages(expensePage.getTotalPages())
            .last(expensePage.isLast())
            .build();
}

    @Override
    public ExpenseResponse getExpenseById(Long id){
        User currentUser = currentUserService.getCurrentUser();
        Expense expense = expenseRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        return mapToResponse(expense);
    }

    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request){
        User currentUser = currentUserService.getCurrentUser();
        Expense expense = expenseRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpense());

        Expense updatedExpense  = expenseRepository.save(expense);
        return mapToResponse(updatedExpense);
    }

    @Override
    public void deleteExpense(Long id) {
        User currentUser = currentUserService.getCurrentUser();
        Expense expense = expenseRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        expenseRepository.delete(expense);
        expenseRepository.delete(expense);
    }

    @Override
    public List<ExpenseResponse> getExpensesByCategory(String category) {
        User currentUser = currentUserService.getCurrentUser();
        return expenseRepository.findByCategoryIgnoreCaseAndUser(category, currentUser)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public String getTotalSummary() {
        User currentUser = currentUserService.getCurrentUser();
        BigDecimal total = expenseRepository.findByUser(currentUser)
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

        User currentUser = currentUserService.getCurrentUser();
        BigDecimal total = expenseRepository.findByExpenseDateBetweenAndUser(startDate, endDate, currentUser)
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return "Total expenses for month " + month + ": ₹" + total;
    }

    @Override
    public byte[] exportExpensesToCsv() {
        User currentUser = currentUserService.getCurrentUser();
        List<Expense> expenses = expenseRepository.findByUser(currentUser);

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
