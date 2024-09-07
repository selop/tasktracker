package net.lopatkin.tasktracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Task Service Tests")
class TaskServiceTests {

    @Autowired
    private TaskService taskService;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        taskService.clearAllTasks();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        new File("tasks.json").delete();
    }

    @Test
    @DisplayName("Adding a task should increase the task list size")
    void testAddTask() {
        taskService.addTask("Test task");
        List<Task> tasks = taskService.getAllTasks();
        assertFalse(tasks.isEmpty());
        assertEquals("Test task", tasks.get(0).description());
    }

    @Test
    @DisplayName("Deleting a task should remove it from the task list")
    void testDeleteTask() {
        taskService.addTask("Task to delete");
        List<Task> tasks = taskService.getAllTasks();
        int taskId = tasks.get(0).id();

        taskService.deleteTask(taskId);

        tasks = taskService.getAllTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    @DisplayName("Listing tasks when the list is empty should display 'No tasks found' for all filters")
    void testListEmptyTasks() {
        taskService.clearAllTasks();

        taskService.listTasks("all");
        assertEquals("No tasks found.", outContent.toString().trim());

        outContent.reset();
        taskService.listTasks("not_done");
        assertEquals("No tasks found.", outContent.toString().trim());

        outContent.reset();
        taskService.listTasks("in_progress");
        assertEquals("No tasks found.", outContent.toString().trim());

        outContent.reset();
        taskService.listTasks("done");
        assertEquals("No tasks found.", outContent.toString().trim());
    }

    @Test
    @DisplayName("Listing tasks should display all added tasks")
    void testListTasks() {
        taskService.addTask("Test task 1");
        taskService.addTask("Test task 2");

        taskService.listTasks("all");

        String output = outContent.toString().trim();
        assertTrue(output.contains("Test task 1"));
        assertTrue(output.contains("Test task 2"));
    }

    @Test
    @DisplayName("Marking a task should update its status")
    void testMarkTask() {
        taskService.addTask("Task to mark");
        List<Task> tasks = taskService.getAllTasks();
        int taskId = tasks.get(0).id();

        taskService.markTask(taskId, "done");

        tasks = taskService.getAllTasks();
        assertEquals("done", tasks.get(0).status());
    }

    @Test
    @DisplayName("Updating a task should change its description")
    void testUpdateTask() {
        taskService.addTask("Original task");
        List<Task> tasks = taskService.getAllTasks();
        int taskId = tasks.get(0).id();

        taskService.updateTask(taskId, "Updated task");

        tasks = taskService.getAllTasks();
        assertEquals("Updated task", tasks.get(0).description());
    }
}
