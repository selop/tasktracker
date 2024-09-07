package net.lopatkin.tasktracker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static net.lopatkin.tasktracker.TaskService.STATUS_NOT_DONE;
import static net.lopatkin.tasktracker.TaskService.STATUS_IN_PROGRESS;
import static net.lopatkin.tasktracker.TaskService.STATUS_DONE;

public class Task {
    private final int id;
    private String description;
    private String status;
    private final LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @JsonCreator
    public Task(
            @JsonProperty("id") int id,
            @JsonProperty("description") String description,
            @JsonProperty("status") String status,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("updatedAt") LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    @JsonProperty
    public LocalDateTime createdAt() {
        return createdAt;
    }

    @JsonProperty
    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        String statusEmoji = switch (status.toLowerCase()) {
            case STATUS_NOT_DONE -> "📝";
            case STATUS_IN_PROGRESS -> "🏗️";
            case STATUS_DONE -> "✅";
            default -> "❓";
        };
        return String.format("🔹 Task #%d %s\n  └─ %s\n  └─ %s\n  └─ Status: %s\n  └─ Created: %s\n  └─ Updated: %s",
                id,
                "━".repeat(20),
                statusEmoji,
                description,
                status,
                createdAt.format(formatter),
                updatedAt.format(formatter));
    }
}