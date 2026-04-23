import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

enum State {
    AVAILABLE,
    STOPPED,
    FAILED
}

class CustomThreadPool {

    int queueCapacity;
    boolean isShutdown;
    CopyOnWriteArrayList<WorkerThread> workerThreadPool;
    BlockingQueue<Runnable> taskQueue;

    public CustomThreadPool(int queueCapacity) {
        this.queueCapacity = queueCapacity;
        this.isShutdown = false;
        this.taskQueue = new ArrayBlockingQueue<>(this.queueCapacity);
        this.workerThreadPool = new CopyOnWriteArrayList<>();

        for(int ind = 0 ; ind < this.queueCapacity ; ind++) {
            WorkerThread worker = new WorkerThread();
            workerThreadPool.add(worker);
            worker.thread.start();
        }
    }

    public void submit(Runnable task) {
        if(task == null) {
            throw new RejectedExecutionException("invalid task");
        }
        
        if(this.isShutdown) {
            throw new RejectedExecutionException("ThreadPool is shutdown");
        }

        boolean isSubmitted = taskQueue.offer(task);
        if(!isSubmitted) {
            throw new RejectedExecutionException("Task queue is full");
        }
    }

    public void shutdown() {
        this.isShutdown = true;
        for (WorkerThread worker : workerThreadPool) {
            worker.stopWorker();
        }
    }

    public void shutdownNow() {
        isShutdown = true;
        taskQueue.clear();
        for (WorkerThread worker : workerThreadPool) {
            worker.thread.interrupt();
        }
    }

    private class WorkerThread implements Runnable {

        private volatile State workerState;
        private final Thread thread;

        public WorkerThread() {
            this.workerState = State.AVAILABLE;
            this.thread = new Thread(this);
        }

        public void stopWorker() {
            this.workerState = State.STOPPED;
            thread.interrupt();
        }

        @Override
        public void run() {

            while (this.workerState.equals(State.AVAILABLE) || !taskQueue.isEmpty()) {
                try {
                    Runnable task = taskQueue.poll(1, TimeUnit.SECONDS);

                    if (task != null) {
                        try {
                            task.run();
                        } catch (Exception e) {
                            System.err.println("Task execution failed: " + e.getMessage());
                        }
                    }
                } catch (InterruptedException e) {
                    if (!this.workerState.equals(State.AVAILABLE)) break;
                }
            }
        }

    }
}

class ThreadPool {
    public static void main(String[] args) {
        
        CustomThreadPool pool = new CustomThreadPool(5);

        for(int index = 1 ; index <= 20 ; index++) {
            int taskId = index;
            pool.submit(() -> {
                System.out.println("executing testing thread number: " + taskId);
            });
        }
    }
}
