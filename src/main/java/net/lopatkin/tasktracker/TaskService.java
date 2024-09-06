package net.lopatkin.tasktracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {

    private static final String FILE_NAME = "tasks.json";
    private static final String STATUS_NOT_DONE = "not_done";
    private static final String STATUS_IN_PROGRESS = "in_progress";
    private static final String STATUS_DONE = "done";

    private final ObjectMapper objectMapper;

    public void addTask(String description) {
        var tasks = getAllTasks();
        var newId = tasks.isEmpty() ? 1 : tasks.get(tasks.size() - 1).id() + 1;
        tasks.add(new Task(newId, description, STATUS_NOT_DONE));
        saveTasks(tasks);
    }

    public void updateTask(int id, String description) {
        var tasks = getAllTasks();
        var updatedTasks = tasks.stream()
            .map(task -> task.id() == id ? new Task(task.id(), description, task.status()) : task)
            .collect(Collectors.toList());
        saveTasks(updatedTasks);
    }

    public void deleteTask(int id) {
        var tasks = getAllTasks();
        tasks.removeIf(task -> task.id() == id);
        saveTasks(tasks);
    }

    public void markTask(int id, String status) {
        var tasks = getAllTasks();
        var updatedTasks = tasks.stream()
            .map(task -> task.id() == id ? new Task(task.id(), task.description(), status) : task)
            .collect(Collectors.toList());
        saveTasks(updatedTasks);
    }

    public void listTasks(String filter) {
        var tasks = getAllTasks();
        var filteredTasks = switch (filter) {
            case STATUS_DONE -> tasks.stream().filter(task -> task.status().equals(STATUS_DONE)).collect(Collectors.toList());
            case STATUS_NOT_DONE -> tasks.stream().filter(task -> task.status().equals(STATUS_NOT_DONE)).collect(Collectors.toList());
            case STATUS_IN_PROGRESS -> tasks.stream().filter(task -> task.status().equals(STATUS_IN_PROGRESS)).collect(Collectors.toList());
            default -> tasks;
        };

        if (filteredTasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            filteredTasks.forEach(task -> System.out.println(task.toString()));
        }
    }

    public List<Task> getAllTasks() {
        var file = new File(FILE_NAME);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, TypeFactory.defaultInstance().constructCollectionType(List.class, Task.class));
            } catch (IOException e) {
                System.err.println("Error reading tasks from file: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    private void saveTasks(List<Task> tasks) {
        try {
            objectMapper.writeValue(new File(FILE_NAME), tasks);
        } catch (IOException e) {
            System.err.println("Error saving tasks to file: " + e.getMessage());
        }
    }
}
