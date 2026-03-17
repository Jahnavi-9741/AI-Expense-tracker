package com.example.expense;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class AIInsightService {

    public String generateInsight(List<Expense> expenses) {
        if (expenses == null || expenses.isEmpty()) {
            return "No expenses added yet. Start tracking to unlock smart insights.";
        }

        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();

        if (total > 10000) {
            return "Your spending is high this month. Consider controlling non-essential expenses.";
        }

        return "Your spending pattern looks healthy and balanced.";
    }

    public List<InsightCard> generateInsightCards(List<Expense> expenses) {
        List<InsightCard> cards = new ArrayList<>();

        if (expenses == null || expenses.isEmpty()) {
            cards.add(new InsightCard("No Data", "0", "Add some expenses to see smart AI insights.", "purple-card"));
            return cards;
        }

        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();

        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            String category = expense.getCategory() == null ? "Other" : expense.getCategory();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + expense.getAmount());
        }

        String topCategory = categoryTotals.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Other");

        double topCategoryAmount = categoryTotals.getOrDefault(topCategory, 0.0);

        long billCount = expenses.stream()
                .filter(e -> "Bill".equalsIgnoreCase(e.getType()))
                .count();

        long dueSoonCount = expenses.stream()
                .filter(e -> e.getDueDate() != null)
                .filter(e -> !e.getDueDate().isBefore(LocalDate.now()))
                .filter(e -> !e.getDueDate().isAfter(LocalDate.now().plusDays(5)))
                .count();

        cards.add(new InsightCard(
                "Total Spending",
                "₹ " + total,
                "Overall money spent across all records.",
                "blue-card"
        ));

        cards.add(new InsightCard(
                "Top Category",
                topCategory,
                "Highest spending is in " + topCategory + " (₹ " + topCategoryAmount + ").",
                "purple-card"
        ));

        cards.add(new InsightCard(
                "Bills Added",
                String.valueOf(billCount),
                "Total bill items tracked in the system.",
                "orange-card"
        ));

        cards.add(new InsightCard(
                "Bills Due Soon",
                String.valueOf(dueSoonCount),
                dueSoonCount > 0 ? "You have bills due within the next 5 days." : "No urgent bill reminders.",
                "red-card"
        ));

        return cards;
    }

    public List<Expense> getUpcomingBills(List<Expense> expenses) {
        return expenses.stream()
                .filter(e -> "Bill".equalsIgnoreCase(e.getType()))
                .filter(e -> e.getDueDate() != null)
                .filter(e -> !e.getDueDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(Expense::getDueDate))
                .toList();
    }
}