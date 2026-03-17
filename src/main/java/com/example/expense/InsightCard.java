package com.example.expense;

public class InsightCard {

    private String title;
    private String value;
    private String message;
    private String colorClass;

    public InsightCard(String title, String value, String message, String colorClass) {
        this.title = title;
        this.value = value;
        this.message = message;
        this.colorClass = colorClass;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public String getColorClass() {
        return colorClass;
    }
}