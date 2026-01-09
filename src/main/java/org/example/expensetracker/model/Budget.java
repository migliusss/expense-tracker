package org.example.expensetracker.model;

public class Budget {
    private final int month;
    private final double monthlyBudget;
    private double usedMonthlyBudget;

    public Budget(int month, double monthlyBudget,  double usedMonthlyBudget) {
        this.month = month;
        this.monthlyBudget = monthlyBudget;
        this.usedMonthlyBudget = usedMonthlyBudget;
    }

    public int getMonth() {
        return month;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public double getUsedMonthlyBudget() {
        return usedMonthlyBudget;
    }

    public void setUsedMonthlyBudget(double usedMonthlyBudget) {
        this.usedMonthlyBudget = usedMonthlyBudget;
    }
}
