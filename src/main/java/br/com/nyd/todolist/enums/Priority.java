package br.com.nyd.todolist.enums;

public enum Priority {
    URGENT(0, "Urgent"),
    HIGH(1, "High"),
    MEDIUM(2, "Medium"),
    LOW(3, "Low");

    private int value;
    private String description;

    Priority(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
