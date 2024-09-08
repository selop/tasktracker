# tasktracker
Task tracker is a project used to track and manage your tasks.

## Description

Task tracker is a simple command-line application that allows you to add, update, delete and list tasks.

## Compiling with GraalVM

This project is compiled with GraalVM to be able to run as a native executable.

### Setup GraalVM using SDKMAN!

1. Install SDKMAN! if you haven't already:
   ```
   curl -s "https://get.sdkman.io" | bash
   ```

2. Install GraalVM:
   ```
   sdk install java 21.0.4-graal
   ```

3. Set GraalVM as your default Java:
   ```
   sdk use java 21.0.4-graal
   ```

### Compile the project

1. Navigate to your project directory:
   ```
   cd path/to/tasktracker
   ```

2. Compile the project using Maven:
   ```
   ./mvnw clean package -Pnative
   ```

3. The native executable will be generated in the `target` directory.

4. Run the application:
   ```
   ./target/tasktracker add "code a todo app"
   ./target/tasktracker list
   ./target/tasktracker update 1 "code a todo app in java"
   ./target/tasktracker delete 1
   ```

Note: Ensure you have the necessary dependencies installed for native compilation on your system.


## Acknowledgments

- This project was inspired by roadmaps.sh (https://roadmap.sh/projects/task-tracker)
- Thanks to Spring Boot and GraalVM to be able to compile Java project into a native executable and start blazingly fast.