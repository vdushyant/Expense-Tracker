package com.dushyant.expensetracker.repository;

import com.dushyant.expensetracker.entity.Budget;
import com.dushyant.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByMonthAndYearAndUser(Integer month, Integer year, User user);
}