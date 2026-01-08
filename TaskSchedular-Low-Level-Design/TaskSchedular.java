import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

enum JobType {
    ONE_TIME,
    FIXED_DELAY,
    FIXED_RATE
}

class ExecutionContext {
    long now;
    long lastScheduledTime;
    long lastCompletionTime;

    public ExecutionContext(long now, long lastScheduledTime, long lastCompletionTime) {
        this.now = now;
        this.lastScheduledTime = lastScheduledTime;
        this.lastCompletionTime = lastCompletionTime;
    }
}

abstract class Task {
    String taskId;
    String description;

    public void execute() {}
}

class Job {
    private final String jobId;
    private final JobMetadata metadata;

    public Job(String jobId, JobMetadata metadata) {
        this.jobId = jobId;
        this.metadata = metadata;
    }

    public String getJobId() {
        return jobId;
    }

    public JobMetadata getMetadata() {
        return metadata;
    }

    public Trigger getTrigger() {
        return metadata.getTrigger();
    }

    public Task getTask() {
        return metadata.getTask();
    }
}

class JobMetadata {
    private final String jobName;
    private final String description;
    private final Task task;
    private final JobType jobType;
    private final Trigger trigger;

    public JobMetadata(
            String jobName,
            String description,
            Task task,
            JobType jobType,
            Trigger trigger
    ) {
        this.jobName = jobName;
        this.description = description;
        this.task = task;
        this.jobType = jobType;
        this.trigger = trigger;
    }

    public String getJobName() {
        return jobName;
    }

    public String getDescription() {
        return description;
    }

    public Task getTask() {
        return task;
    }

    public JobType getJobType() {
        return jobType;
    }

    public Trigger getTrigger() {
        return trigger;
    }
}

interface Trigger {
    long nextExecutionTime(ExecutionContext ctx);
}

class OneTimeTrigger implements Trigger {

    long runAt;
    private final AtomicBoolean executed = new AtomicBoolean(false);

    public long nextExecutionTime(ExecutionContext ctx) {
        if (!executed.compareAndSet(false, true)) {
            return -1;
        }
        return runAt;
    }
}

class FixedDelayTrigger implements Trigger {

    long delay;

    public long nextExecutionTime(ExecutionContext ctx) {
        if (ctx.lastCompletionTime < 0) {
            return ctx.now + delay;
        }
        return ctx.lastCompletionTime + delay;
    }
}


class FixedRateTrigger implements Trigger {

    long interval;

    public long nextExecutionTime(ExecutionContext ctx) {
        if (ctx.lastScheduledTime < 0) {
            return ctx.now + interval;
        }
        return ctx.lastScheduledTime + interval;
    }
}

class ScheduledTask implements Delayed {

    private final Job job;

    private volatile long lastScheduledTime = -1;
    private volatile long lastCompletionTime = -1;
    private volatile long nextRunTime;

    public ScheduledTask(Job job) {
        this.job = job;
        this.nextRunTime = job.getTrigger()
                .nextExecutionTime(new ExecutionContext(
                        System.currentTimeMillis(), -1, -1
                ));
    }

    public Job getJob() {
        return job;
    }

    public long getNextRunTime() {
        return nextRunTime;
    }

    public void setLastScheduledTime(long time) {
        this.lastScheduledTime = time;
    }

    public void setLastCompletionTime(long time) {
        this.lastCompletionTime = time;
    }

    public synchronized boolean computeNextRunTime() {
        ExecutionContext ctx = new ExecutionContext(
                System.currentTimeMillis(),
                lastScheduledTime,
                lastCompletionTime
        );

        long next = job.getTrigger().nextExecutionTime(ctx);
        if (next < 0) return false;

        this.nextRunTime = next;
        return true;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long delay = nextRunTime - System.currentTimeMillis();
        return unit.convert(delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        ScheduledTask other = (ScheduledTask) o;
        return Long.compare(this.nextRunTime, other.nextRunTime);
    }
}



class TaskStore {
    ConcurrentHashMap<String, Job> jobStore = new ConcurrentHashMap<>();
    static TaskStore instance;

    private TaskStore() {}

    public static TaskStore getInstance() {
        if(instance == null) {
            synchronized (TaskStore.class) {
                if(instance == null) {
                    instance = new TaskStore();
                }
            }
        }
        return instance;
    }

    public Job getJob(String jobId) {
        return jobStore.get(jobId);
    }

    public void addJob(String jobId, Job job) {
        jobStore.put(jobId, job);
    }

    public Job removeJob(String jobId) {
        return jobStore.remove(jobId);
    }

    public boolean containsJob(String jobId) {
        return jobStore.containsKey(jobId);
    }
}

class SchedulerEngine {

    private final DelayQueue<ScheduledTask> queue = new DelayQueue<>();
    private final ConcurrentHashMap<String, ScheduledTask> taskIndex = new ConcurrentHashMap<>();
    private final SchedulerExecutor executor;
    private volatile boolean running = true;
    private final Thread schedulerThread;

    public SchedulerEngine(SchedulerExecutor executor) {
        this.executor = executor;
        this.schedulerThread = new Thread(this::runLoop, "scheduler-engine-thread");
        this.schedulerThread.setDaemon(true);
        this.schedulerThread.start();
    }

    private void runLoop() {
        while (running) {
            try {
                // Blocks until the next task is ready
                ScheduledTask task = queue.take();

                // Record scheduled time BEFORE execution
                task.setLastScheduledTime(task.getNextRunTime());

                // Dispatch to executor (non-blocking)
                executor.execute(task, () -> onTaskCompletion(task));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void onTaskCompletion(ScheduledTask task) {
        task.setLastCompletionTime(System.currentTimeMillis());
        boolean shouldReschedule = task.computeNextRunTime();

        if (shouldReschedule && running) {
            queue.put(task);
        }
    }

    public void submitJob(Job job) {
        ScheduledTask task = new ScheduledTask(job);
        if (task.getNextRunTime() < 0) return;

        taskIndex.put(job.getJobId(), task);
        queue.put(task);
    }

    public void cancelJob(String jobId) {
        ScheduledTask task = taskIndex.remove(jobId);
        if (task != null) {
            queue.remove(task);
        }
    }

    public void shutdown() {
        running = false;
        schedulerThread.interrupt();
    }
}

class SchedulerExecutor {

    private final ExecutorService pool;

    public SchedulerExecutor(int resource) {
        pool = Executors.newFixedThreadPool(resource);
    }

    public void execute(ScheduledTask task, Runnable onComplete) {
        pool.submit(() -> {
            try {
                task.getJob().getTask().execute();
            } finally {
                task.setLastCompletionTime(System.currentTimeMillis());
                onComplete.run();
            }
        });
    }

    public void shutdown() {
        pool.shutdown();
    }
}

class SchedulerManager {

    private static volatile SchedulerManager instance;

    private final SchedulerExecutor schedulerExecutor;
    private final SchedulerEngine schedulerEngine;
    private final TaskStore taskStore;

    private SchedulerManager(int resources) {
        this.schedulerExecutor = new SchedulerExecutor(resources);
        this.schedulerEngine = new SchedulerEngine(schedulerExecutor);
        this.taskStore = TaskStore.getInstance();
    }

    public static SchedulerManager getInstance(int resources) {
        if (instance == null) {
            synchronized (SchedulerManager.class) {
                if (instance == null) {
                    instance = new SchedulerManager(resources);
                }
            }
        }
        return instance;
    }

    public SchedulerEngine getSchedulerEngine() {
        return schedulerEngine;
    }

    /**
     * Create and schedule a new job
     */
    public String createJob(JobMetadata jobMetadata) {
        validate(jobMetadata);

        String jobId = generateJobId();
        Job job = new Job(jobId, jobMetadata);

        taskStore.addJob(jobId, job);
        schedulerEngine.submitJob(job);

        return jobId;
    }

    /**
     * Delete a job
     */
    public void deleteJob(String jobId) {
        schedulerEngine.cancelJob(jobId);
        taskStore.removeJob(jobId);
    }

    /**
     * Update = delete + recreate
     */
    public void updateJob(String jobId, JobMetadata jobMetadata) {
        deleteJob(jobId);
        createJob(jobMetadata);
    }

    private void validate(JobMetadata metadata) {
        if (metadata == null) {
            throw new IllegalArgumentException("Job metadata cannot be null");
        }
        if (metadata.getTask() == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (metadata.getTrigger() == null) {
            throw new IllegalArgumentException("Trigger cannot be null");
        }
    }

    private String generateJobId() {
        return UUID.randomUUID().toString();
    }
}

public class TaskSchedular {

    public static void main(String[] args) throws InterruptedException {

        // Initialize scheduler with 2 worker threads
        SchedulerManager schedulerManager = SchedulerManager.getInstance(2);

        // Define a task
        Task printTask = new Task() {
            @Override
            public void execute() {
                System.out.println(
                        "Task executed at " + System.currentTimeMillis()
                );
            }
        };

        // Trigger: run every 2 seconds (fixed rate)
        Trigger trigger = new FixedRateTrigger() {{
            interval = 2000;
        }};

        // Job metadata
        JobMetadata metadata = new JobMetadata(
                "print-job",
                "Prints timestamp every 2 seconds",
                printTask,
                JobType.FIXED_RATE,
                trigger
        );

        // Create job
        String jobId = schedulerManager.createJob(metadata);

        System.out.println("Job scheduled with ID: " + jobId);

        // Let it run for 10 seconds
        Thread.sleep(10_000);

        // Cancel job
        schedulerManager.deleteJob(jobId);
        System.out.println("Job cancelled");

    }
}
