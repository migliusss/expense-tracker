package org.example.expensetracker.model;

import java.time.LocalDate;
public class Expense {
    private static int nextId = 1;
    private final int id;
    private LocalDate date;
    private String category;
    private String description;
    private double amount;

    public Expense(LocalDate date, String category, String description, double amount) {
        this.id = nextId++;
        this.date = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return this.id + ", " + this.date + ", " + this.category + ", " + this.description + ", " + this.amount;
    }
}
