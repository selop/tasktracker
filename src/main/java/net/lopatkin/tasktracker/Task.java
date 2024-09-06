package net.lopatkin.tasktracker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {
    private final int id;
    private final String description;
    private String status;

    @JsonCreator
    public Task(
            @JsonProperty("id") int id,
            @JsonProperty("description") String description,
            @JsonProperty("status") String status) {
        this.id = id;
        this.description = description;
        this.status = status;
    }

    @JsonProperty
    public int id() {
        return id;
    }

    @JsonProperty
    public String description() {
        return description;
    }

    @JsonProperty
    public String status() {
        return status;
    }

    @JsonProperty
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String statusEmoji = switch (status.toLowerCase()) {
            case "todo" -> "📝";
            case "in progress" -> "🏗️";
            case "done" -> "✅";
            default -> "❓";
        };
        return String.format("🔹 Task #%d %s\n  └─ %s\n  └─ Status: %s %s",
                id, 
                "━".repeat(20),
                description,
                status,
                statusEmoji);
    }
}