import java.util.*;

enum TaskType {
    STORY,
    BUG,
    EPIC,
    SPIKE
}

enum TaskStatus {
    PENDING,
    BACKLOG,
    IN_PROGRESS,
    DONE
}

class Task {

    private String taskId;
    private String title;
    private String description;
    private TaskStatus taskStatus;
    private TaskType taskType;
    private User assigned;
    private User assignee;
    
    public Task() {
        this.taskId = "TS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.taskStatus = TaskStatus.BACKLOG;
    }

    public String getTaskId() {
        return taskId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public User getAssigned() {
        return assigned;
    }

    public void setAssigned(User assigned) {
        this.assigned = assigned;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }
    
    public void printTaskDetails() {
        System.out.println("Task ID: " + taskId);
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Task Status: " + taskStatus);
        System.out.println("Task Type: " + taskType);
        System.out.println("Assigned To: " + (assigned != null ? assigned.getUserName() : "Not Assigned"));
        System.out.println("Assignee: " + (assignee != null ? assignee.getUserName() : "Not Assigned"));
    }

}

class User {

    private String userId;
    private String userName;
    private String emailId;
    private String designation;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
    
    public void sendMail(Task task) {
        System.out.println("Sending email to " + this.emailId + " regarding task " + task.getTaskId() + ": " + task.getTitle());
    }

}

class TaskManager {
    
    List<Task> taskList = new ArrayList<>();
    
    public void createTask(Task task) {
        taskList.add(task);
        sendReminder(task);
    }
    
    public void removeTask(String taskId) {
        Task task = getTask(taskId);
        if(task != null) {
            taskList.remove(task);
        }
    }
    
    public void updateTaskStatus(String taskId, TaskStatus taskStatus) {
        Task task = getTask(taskId);
        if(task != null) {
            task.setTaskStatus(taskStatus);
            sendReminder(task);
        }
    }
    
    public void updateTaskAssigned(String taskId, User assigned) {
        Task task = getTask(taskId);
        if(task != null) {
            task.setAssigned(assigned);
            sendReminder(task);
        }
    }
    
    public void updateTask(String taskId, Task updatedTask) {
        Task task = getTask(taskId);
        if(task != null) {
            if(updatedTask.getTitle() != null) {
                task.setTitle(updatedTask.getTitle());
            }
            
            if(updatedTask.getAssigned() != null) {
                task.setAssigned(updatedTask.getAssigned());
            }
            
            if(updatedTask.getAssignee() != null) {
                task.setAssignee(updatedTask.getAssigned());
            }
            
            if(updatedTask.getTaskStatus() != null) {
                task.setTaskStatus(updatedTask.getTaskStatus());
            }
            
            if(updatedTask.getTaskType() != null) {
                task.setTaskType(updatedTask.getTaskType());
            }
            
            sendReminder(task);
        }
    }
    
    public List<Task> searchTask(Task taskFilter) {
        List<Task> searchedTaskList = new ArrayList<>();
        for(Task task : taskList) {
            
            boolean taskTitle = taskFilter.getTitle() != null && taskFilter.getTitle() == task.getTitle();
            boolean taskId = taskFilter.getTaskId() != null && taskFilter.getTaskId() == task.getTaskId();
            boolean taskAssigned = taskFilter.getAssigned() != null && taskFilter.getAssigned() == task.getAssigned();
            boolean taskAssignee = taskFilter.getAssignee() != null && taskFilter.getAssignee() == task.getAssignee();
            boolean taskType = taskFilter.getTaskType() != null && taskFilter.getTaskType() == task.getTaskType();
            
            if(taskTitle || taskId || taskAssignee || taskAssigned || taskType) {
                searchedTaskList.add(task);
            }
        }
        return searchedTaskList;
    }
    
    public void sendReminder(Task task) {
        if(task.getAssigned() != null) {
            task.getAssigned().sendMail(task);
        }
        
        if(task.getAssignee() != null) {
            task.getAssignee().sendMail(task);
        }
    }
    
    public Task getTask(String taskId) {
        for(Task task : taskList) {
            if(task.getTaskId() == taskId) {
                return task;
            }
        }
        
        return null;
    } 
}

public class Main {
	public static void main(String[] args) {
	    
	    // create task manager
	    TaskManager taskManager = new TaskManager();
		
		// create user
		User user1 = new User();
		user1.setUserName("jimmy");
		user1.setDesignation("software engineer");
		user1.setEmailId("jimmy@gmail.com");
		user1.setUserId("US-77");
		
		User user2 = new User();
		user2.setUserName("jimmy");
		user2.setDesignation("software engineer");
		user2.setEmailId("jimmy@gmail.com");
		user2.setUserId("US-77");
		
		// create task
		Task task1 = new Task();
		task1.setTitle("create design of task planner");
		task1.setTaskType(TaskType.EPIC);
		task1.setDescription("create design on task management system");
		task1.setAssigned(user2);
		task1.setAssignee(user1);
		taskManager.createTask(task1);
		
		Task task2 = new Task();
		task2.setTitle("implement design of task planner");
		task2.setTaskType(TaskType.STORY);
		task2.setDescription("implement design on task management system");
		task2.setAssigned(user1);
		task2.setAssignee(user2);
		taskManager.createTask(task2);
		
		// user2 wants to set status to progress
		taskManager.updateTaskStatus(task1.getTaskId(), TaskStatus.IN_PROGRESS);
		
		// user1 search all the tasks by task type
		Task taskFilter = new Task();
		taskFilter.setTaskType(TaskType.EPIC);
		List<Task> searchedTaskList = taskManager.searchTask(taskFilter);
		for(Task task : searchedTaskList) {
		    task.printTaskDetails();
		}
	}
}
