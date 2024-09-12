# Task Management System

## Overview
This project implements a **Task Management System** that allows users to create and manage tasks. The system supports task assignment, task status updates, task searching, and email reminders for users assigned to tasks.

## Features
- Create Users: Each user has attributes such as name, designation, email, and user ID.
- Create Tasks: Tasks are created with details like title, description, task type, and task status.
- Assign Users: Tasks can be assigned to one or more users.
- Update Task Status: Users can update the status of tasks (e.g., PENDING, IN_PROGRESS, DONE).
- Search Tasks: Tasks can be searched based on different filters (e.g., task type).
- Email Reminders: Users receive email reminders when tasks are updated.

## Example Workflow

1. **Create Users:**
   - Two users (`user1` and `user2`) are created in the system with attributes such as:
     - **User 1**: name, designation, email, user ID
     - **User 2**: name, designation, email, user ID

2. **Create Tasks:**
   - Two tasks are created:
     - **Task 1**: Design the task management system (EPIC)
     - **Task 2**: Implement the design of the task management system (STORY)

3. **Assign Users:**
   - `user2` is assigned to **Task 1** and `user1` is the assignee.
   - `user1` is assigned to **Task 2** and `user2` is the assignee.

4. **Update Task Status:**
   - `user2` updates the status of **Task 1** to "In Progress."

5. **Search Tasks:**
   - `user1` searches for tasks of type "EPIC" and prints the task details.

## Code Structure

### Enums
- **TaskType**: Enum representing different task types: `STORY`, `BUG`, `EPIC`, `SPIKE`.
- **TaskStatus**: Enum representing different task statuses: `PENDING`, `BACKLOG`, `IN_PROGRESS`, `DONE`.

### Classes
- **Task**:
  - Attributes:
    - `taskId`: Unique identifier for the task.
    - `title`: Title of the task.
    - `description`: Description of the task.
    - `taskStatus`: Current status of the task (`PENDING`, `IN_PROGRESS`, etc.).
    - `taskType`: Type of the task (`EPIC`, `STORY`, etc.).
    - `assigned`: Users assigned to the task.
    - `assignee`: The user responsible for completing the task.
  - **Methods**:
    - `printTaskDetails()`: Prints the details of the task.

- **User**:
  - Attributes:
    - `userId`: Unique identifier for the user.
    - `userName`: Name of the user.
    - `emailId`: Email address of the user.
    - `designation`: Designation of the user.
  - **Methods**: Users receive email reminders for tasks assigned to them.

- **TaskManager**:
  - **Methods**:
    - `createTask(Task task)`: Creates a new task and sends email reminders to assigned users.
    - `updateTaskStatus(String taskId, TaskStatus status)`: Updates the status of a task and notifies the users.
    - `updateTaskAssigned(String taskId, User assigned)`: Updates the assigned user and notifies them.
    - `searchTask(Task taskFilter)`: Searches for tasks based on filters such as task type or status.
    - `sendReminder(Task task)`: Sends reminder emails to the assigned and assignee users.

## Future Enhancements
- Implement priority levels for tasks (e.g., high, medium, low).
- Add functionality to handle comments on tasks.
- Integrate with a real email service for sending notifications.
- Implement deadlines and due dates for tasks.

## License
This project is licensed under the MIT License.
