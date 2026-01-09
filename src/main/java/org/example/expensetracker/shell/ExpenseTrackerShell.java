package org.example.expensetracker.shell;

import org.example.expensetracker.model.Budget;
import org.example.expensetracker.model.Expense;
import org.example.expensetracker.repository.SaveRepository;
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

    public ExpenseTrackerShell(SaveRepository saveRepository) {
        this.saveRepository = saveRepository;
    }

    @ShellMethod(key = "add", value="Add a new expense.")
    public void add(@ShellOption(defaultValue = "Other") String category, @ShellOption String description, @ShellOption double amount) {
        LocalDate date = LocalDate.now();

        if (amount < 0) {
            System.out.println("Amount for an expense cannot be negative :/");

            return;
        }

        List<Budget> budgets = saveRepository.getBudgets();

        if (!budgets.isEmpty()) {
            int expenseMonth = date.getMonthValue();

            for (Budget budget : budgets) {
                if (budget.getMonth() == expenseMonth) {
                    double newUsedMonthlyBudget = budget.getUsedMonthlyBudget() + amount;
                    budget.setUsedMonthlyBudget(newUsedMonthlyBudget);

                    if (newUsedMonthlyBudget > budget.getMonthlyBudget()) {
                        System.out.println("Monthly Budget Warning!");
                        System.out.println(
                                "You have exceeded the "
                                + Month.of(expenseMonth).getDisplayName(TextStyle.FULL, Locale.getDefault())
                                + " budget: $"
                                + budget.getMonthlyBudget()
                        );
                        System.out.println("Current used monthly budget: $" + budget.getUsedMonthlyBudget());
                    }
                }
            }
        }

        Expense expense = new Expense(date, category, description, amount);

        saveRepository.addExpense(expense);

        System.out.println("Expense added successfully (ID: " + expense.getId() + ") :)");
    }

    @ShellMethod(key = "update", value = "Update an expense.")
    public void update(
            @ShellOption int id,
            @ShellOption String category,
            @ShellOption String description,
            @ShellOption double amount
    ) {
        List<Expense> expenses = saveRepository.getExpenses();

        if (id < 0 || id > expenses.size()) {
            System.out.println("Expense with ID " + id + " does not exist in expense list :/");

            return;
        }

        Expense expense = expenses.get(id - 1);

        if (category != null && !category.isEmpty()) {
            expense.setCategory(category);
        }

        if (description != null && !description.isEmpty()) {
            expense.setDescription(description);
        }

        if (amount >= 0.0) {
            expense.setAmount(amount);
        }

        System.out.println("Expense with ID " + id + " updated successfully :)");
    }

    @ShellMethod(key = "delete", value = "Delete an expense.")
    public void delete(@ShellOption int id) {
        List<Expense> expenses = saveRepository.getExpenses();

        if (!expenses.removeIf(e -> e.getId() == id)) {
            System.out.println("Expense with ID " + id + " does not exist in expense list :(");

            return;
        }

        System.out.println("Expense deleted successfully :)");
    }

    @ShellMethod(key = "list", value = "List all expenses.")
    public void list(@ShellOption(defaultValue = ShellOption.NULL) String category) {
        List<Expense> expenses = saveRepository.getExpenses();

        if (category != null) {
           expenses = expenses
                   .stream()
                   .filter(e -> e
                           .getCategory()
                           .equals(category)
                   )
                   .toList();
        }

        String format = "%-" + 6 + "s%-" + 14 + "s%-" + 24 + "s%-" + 24 + "s%" + 14 + "s%n";
        System.out.printf(format, "ID", "Date", "Category", "Description", "Amount");

        for (Expense expense : expenses) {
            System.out.printf(
                    format,
                    expense.getId(),
                    expense.getDate(),
                    expense.getCategory(),
                    expense.getDescription(),
                    "$" + expense.getAmount()
            );
        }
    }

    @ShellMethod(key = "summary", value = "Summarize by month.")
    public void summary(@ShellOption(defaultValue = ShellOption.NULL) Integer month) {
        if (month == null) {
            double sum = 0;
            List<Expense> expenses = saveRepository.getExpenses();

            for (Expense expense : expenses) {
                sum += expense.getAmount();
            }

            System.out.println("Total expenses: $" + sum);

            return;
        }

        if (month < 1 || month > 12) {
            return;
        }

        double sum = 0;
        List<Expense> expenses = saveRepository.getExpenses();

        for (Expense expense : expenses) {
            if (expense.getDate().getMonthValue() == month) {
                sum += expense.getAmount();
            }
        }

        System.out.println("Total expenses for " +  Month.of(month).getDisplayName(TextStyle.FULL, Locale.getDefault()) + ": $" + sum);
    }

    @ShellMethod(key = "set budget", value = "Set budget for a month.")
    public void setBudget(@ShellOption int month, @ShellOption double monthlyBudget) {
        if (month < 1 || month > 12) {
            System.out.println("Invalid month :/ ");

            return;
        }

        List<Budget> budgets = saveRepository.getBudgets();

        for (Budget budget : budgets) {
            if (budget.getMonth() == month) {
                System.out.println(
                        "Budget for "
                                + Month.of(month).getDisplayName(TextStyle.FULL, Locale.getDefault())
                                + " already exists: $" + budget.getMonthlyBudget()
                );

                return;
            }
        }

        Budget budget = new Budget(month, monthlyBudget, 0);

        saveRepository.setBudget(budget);

        System.out.println("Budget $" + monthlyBudget + " for " + Month.of(month).getDisplayName(TextStyle.FULL, Locale.getDefault()) + " added successfully :)");
    }

    @ShellMethod(key = "export", value = "Export expenses to a CSV file.")
    public void export() {
        saveRepository.convertToCSV();
    }
}
