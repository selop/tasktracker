package net.lopatkin.tasktracker;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;

@SpringBootApplication
public class TaskTrackerApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TaskTrackerApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.setLogStartupInfo(false);
		System.exit(SpringApplication.exit(app.run(args)));
	}

	@Bean
	public CommandLineRunner commandLineRunner(TaskService taskService) {
		return args -> {
			if (args.length == 0) {
				System.out.println("Please provide a command. Use 'help' for usage information.");
				return;
			}

			Map<String, BiConsumer<String[], TaskService>> commands = Map.of(
				"add", this::addTodo,
				"update", this::updateTodo,
				"delete", this::deleteTodo,
				"mark-in-progress", this::markInProgressTodo,
				"mark-done", this::markDoneTodo,
				"list", this::listTodos,
				"help", (a, t) -> printHelp()
			);

			BiConsumer<String[], TaskService> command = commands.get(args[0]);
			if (command != null) {
				command.accept(args, taskService);
			} else {
				System.out.println("Unknown command. Use 'help' for usage information.");
			}
		};
	}

	private void addTodo(String[] args, TaskService taskService) {
		if (validateArgs(args, 2, "Please provide a task description.")) {
			taskService.addTask(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
			System.out.println("Task added successfully");
		}
	}

	private void updateTodo(String[] args, TaskService taskService) {
		if (validateArgs(args, 3, "Please provide a task ID and new description.") && validateNumericId(args[1])) {
			taskService.updateTask(Integer.parseInt(args[1]), String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
			System.out.println("Task updated successfully.");
		}
	}

	private void deleteTodo(String[] args, TaskService taskService) {
		if (validateArgs(args, 2, "Please provide a task ID.") && validateNumericId(args[1])) {
			taskService.deleteTask(Integer.parseInt(args[1]));
			System.out.println("Task deleted successfully.");
		}
	}

	private void markInProgressTodo(String[] args, TaskService taskService) {
		if (validateArgs(args, 2, "Please provide a task ID.") && validateNumericId(args[1])) {
			taskService.markTask(Integer.parseInt(args[1]), TaskService.STATUS_IN_PROGRESS);
			System.out.println("Task marked as in progress successfully.");
		}
	}

	private void markDoneTodo(String[] args, TaskService taskService) {
		if (validateArgs(args, 2, "Please provide a task ID.") && validateNumericId(args[1])) {
			taskService.markTask(Integer.parseInt(args[1]), TaskService.STATUS_DONE);
			System.out.println("Task marked as done successfully.");
		}
	}

	private void listTodos(String[] args, TaskService taskService) {
		String filter = args.length > 1 ? args[1] : "all";
		taskService.listTasks(filter);
	}

	private boolean validateArgs(String[] args, int requiredCount, String errorMessage) {
		if (args.length < requiredCount) {
			System.out.println(errorMessage);
			return false;
		}
		return true;
	}

	private boolean validateNumericId(String id) {
		try {
			Integer.parseInt(id);
			return true;
		} catch (NumberFormatException e) {
			System.out.println("Invalid task ID. Please provide a numeric ID.");
			return false;
		}
	}

	private void printHelp() {
		System.out.println("Task Tracker - Usage:");
		System.out.println("  add <description>              - Add a new task");
		System.out.println("  update <id> <description>      - Update a task");
		System.out.println("  delete <id>                    - Delete a task");
		System.out.println("  mark-in-progress <id>          - Mark a task as in progress");
		System.out.println("  mark-done <id>                 - Mark a task as done");
		System.out.println("  list [filter]                  - List tasks (filter: all, done, todo, in-progress)");
		System.out.println("  help                           - Show this help message");
	}
}
