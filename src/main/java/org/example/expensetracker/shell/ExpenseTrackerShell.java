package org.example.expensetracker.shell;

import org.example.expensetracker.model.Expense;
import org.example.expensetracker.repository.SaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@ShellComponent
public class ExpenseTrackerShell {
    private final SaveRepository saveRepository;

    @Autowired
    public ExpenseTrackerShell(SaveRepository saveRepository) {
        this.saveRepository = saveRepository;
    }

    @ShellMethod(key = "add", value="Add a new expense.")
    public void add(@ShellOption(defaultValue = "Expense") String category, @ShellOption String description, @ShellOption double amount) {
        LocalDate localDate = LocalDate.now();

        Expense expense = new Expense(localDate, category, description, amount);

        saveRepository.add(expense);

        System.out.println("Expense added successfully (ID: " + expense.getId() + ")");
    }

    @ShellMethod(key = "list", value = "List all expenses.")
    public void list(@ShellOption(defaultValue = ShellOption.NULL) String category) {
        List<Expense> expenses;

        if (category != null) {
            expenses = saveRepository.getByCategory(category);
        } else {
            expenses = saveRepository.get();
        }

        String print = "%-" + 6 + "s%-" + 14 + "s%-" + 14 + "s%-" + 24 + "s%" + 10 + "s%n";

        System.out.printf(print, "ID", "Date", "Category", "Description", "Amount");

        for (Expense expense : expenses) {
            System.out.printf(
                    print,
                    expense.getId(),
                    expense.getDate(),
                    expense.getCategory(),
                    expense.getDescription(),
                    expense.getAmount()
            );
        }
    }

    @ShellMethod(key = "summary", value = "Summarize by month.")
    public void summary(@ShellOption(defaultValue = ShellOption.NULL) Integer month) {
        if (month == null) {
            double sum = 0;
            List<Expense> expenses = saveRepository.get();

            for (Expense expense : expenses) {
                sum += expense.getAmount();
            }

            System.out.println("Total expenses: NOK" + sum);

            return;
        }

        if (month < 1 || month > 12) {
            return;
        }

        double sum = 0;
        List<Expense> expenses = saveRepository.get();

        for (Expense expense : expenses) {
            if (expense.getDate().getMonthValue() == month) {
                sum += expense.getAmount();
            }
        }

        System.out.println("Total expenses for " +  Month.of(month).getDisplayName(TextStyle.FULL, Locale.getDefault()) + ": " + sum);
    }

    @ShellMethod(key = "delete", value = "Delete an expense.")
    public void delete(@ShellOption int id) {
        List<Expense> expenses = saveRepository.get();

        expenses.removeIf(e -> e.getId() == id);

        System.out.println("Expense deleted successfully");
    }

    @ShellMethod(key = "export", value = "Export expenses to a CSV file.")
    public void export() {
        saveRepository.convertToCSV();
    }
}
