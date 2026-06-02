package com.dushyant.expensetracker.repository;

import com.dushyant.expensetracker.entity.Expense;
import com.dushyant.expensetracker.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByUser(User user, Pageable pageable);

    Optional<Expense> findByIdAndUser(Long id, User user);

    List<Expense> findByCategoryIgnoreCaseAndUser(String category, User user);

    List<Expense> findByExpenseDateBetweenAndUser(LocalDate startDate, LocalDate endDate, User user);

    List<Expense> findByUser(User user);
}