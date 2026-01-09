package org.example.expensetracker.repository;

import org.example.expensetracker.model.Expense;

import java.util.List;

public interface SaveRepository {
    void add(Expense expense);

    List<Expense> get();

    List<Expense> getByCategory(String category);

    void convertToCSV();
}
