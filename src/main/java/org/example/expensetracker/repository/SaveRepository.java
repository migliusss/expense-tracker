package org.example.expensetracker.repository;

import org.example.expensetracker.model.Budget;
import org.example.expensetracker.model.Expense;

import java.util.List;

public interface SaveRepository {
    void addExpense(Expense expense);

    List<Expense> getExpenses();

    List<Budget> getBudgets();

    void setBudget(Budget budget);

    void convertToCSV();
}
