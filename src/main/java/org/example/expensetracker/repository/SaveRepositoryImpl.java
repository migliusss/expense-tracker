package org.example.expensetracker.repository;

import com.opencsv.CSVWriter;
import org.example.expensetracker.model.Budget;
import org.example.expensetracker.model.Expense;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SaveRepositoryImpl implements SaveRepository {
    private final List<Expense> expenses = new ArrayList<>();
    private final List<Budget>  budgets = new ArrayList<>();

    @Override
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    @Override
    public List<Expense> getExpenses() {
        return expenses;
    }

    @Override
    public void setBudget(Budget budget) {
        budgets.add(budget);
    }

    @Override
    public List<Budget> getBudgets() {
        return budgets;
    }

    @Override
    public void convertToCSV() {
        String filePath = "expenses.csv";
        List<String[]> csvData = new ArrayList<>();

        csvData.add(new String[]{"ID", "Date", "Category", "Description", "Amount"});

        for(Expense expense : expenses){
            csvData.add(new String[]{expense.toString()});
        }

        try(CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath))) {
            csvWriter.writeAll(csvData);
            System.out.println("Your " + filePath + " was created successfully :)");
        } catch(IOException e) {
            System.err.println("Error writing to CSV file :/ " + e.getMessage());
        }
    }
}
