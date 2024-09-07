package net.lopatkin.tasktracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class TaskTrackerApplicationTests {

    @Autowired
    private CommandLineRunner commandLineRunner;

    @Autowired
    private TaskService taskService;

	@BeforeEach
	void setUp() {
		taskService.clearAllTasks();
	}

	@Test
	@DisplayName("Add a new task")
	void testAddTask(CapturedOutput output) throws Exception {
		commandLineRunner.run("add", "Test task");
		assertThat(output.getOut()).contains("Task added successfully");
	}

	@Test
	@DisplayName("List all tasks")
	void testListTasks(CapturedOutput output) throws Exception {
		taskService.addTask("Test task for listing");
		commandLineRunner.run("list");
		assertThat(output.getOut()).contains("Test task for listing");
	}

	@Test
	@DisplayName("Update an existing task")
	void testUpdateTask(CapturedOutput output) throws Exception {
		taskService.addTask("Original task");
		commandLineRunner.run("update", "1", "Updated task");
		assertThat(output.getOut()).contains("Task updated successfully");
		
		commandLineRunner.run("list");
		assertThat(output.getOut()).contains("Updated task");
	}

	@Test
	@DisplayName("Delete a task")
	void testDeleteTask(CapturedOutput output) throws Exception {
		taskService.addTask("Task to delete");
		commandLineRunner.run("delete", "1");
		assertThat(output.getOut()).contains("Task deleted successfully");
		
		commandLineRunner.run("list");
		assertThat(output.getOut()).doesNotContain("Task to delete");
	}

	@Test
	@DisplayName("Mark a task as in progress")
	void testMarkInProgressTask(CapturedOutput output) throws Exception {
		taskService.addTask("Task in progress");
		commandLineRunner.run("mark-in-progress", "1");
		assertThat(output.getOut()).contains("Task marked as in progress successfully");
		
		commandLineRunner.run("list", "in-progress");
		assertThat(output.getOut()).contains("Task in progress");
	}

	@Test
	@DisplayName("Mark a task as done")
	void testMarkDoneTask(CapturedOutput output) throws Exception {
		taskService.addTask("Task to complete");
		commandLineRunner.run("mark-done", "1");
		assertThat(output.getOut()).contains("Task marked as done successfully");
		
		commandLineRunner.run("list", "done");
		assertThat(output.getOut()).contains("Task to complete");
	}

	@Test
	@DisplayName("List tasks with 'todo' filter")
	void testListTasksWithFilter(CapturedOutput output) throws Exception {
		taskService.addTask("Todo task");
		taskService.addTask("In progress task");
		taskService.markTask(2, TaskService.STATUS_IN_PROGRESS);
		taskService.addTask("Done task");
		taskService.markTask(3, TaskService.STATUS_DONE);

		commandLineRunner.run("list", TaskService.STATUS_NOT_DONE);
		assertThat(output.getOut())
			.contains("Task #1")
			.contains("Todo task")
			.doesNotContain("In progress task")
			.doesNotContain("Done task");
	}

	@Test
	@DisplayName("List in-progress tasks")
	void testListInProgressTasks(CapturedOutput output) throws Exception {
		taskService.addTask("Todo task");
		taskService.addTask("In progress task");
		taskService.markTask(2, TaskService.STATUS_IN_PROGRESS);
		taskService.addTask("Done task");
		taskService.markTask(3, TaskService.STATUS_DONE);

		commandLineRunner.run("list", TaskService.STATUS_IN_PROGRESS);
		assertThat(output.getOut())
			.contains("Task #2")
			.contains("In progress task")
			.doesNotContain("Todo task")
			.doesNotContain("Done task");
	}

	@Test
	@DisplayName("List done tasks")
	void testListDoneTasks(CapturedOutput output) throws Exception {
		taskService.addTask("Todo task");
		taskService.addTask("In progress task");
		taskService.markTask(2, TaskService.STATUS_IN_PROGRESS);
		taskService.addTask("Done task");
		taskService.markTask(3, TaskService.STATUS_DONE);

		commandLineRunner.run("list", TaskService.STATUS_DONE);
		assertThat(output.getOut())
			.contains("Task #3")
			.contains("Done task")
			.doesNotContain("Todo task")
			.doesNotContain("In progress task");
	}
}
