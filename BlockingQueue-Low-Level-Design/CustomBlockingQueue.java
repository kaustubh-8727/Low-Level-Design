
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class CustomBlockingQueueService<T> {

    private final Queue<T> queue = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    private final int capacity;

    public CustomBlockingQueueService() {
        this.capacity = Integer.MAX_VALUE;
    }

    public CustomBlockingQueueService(int capacity) {
        this.capacity = capacity;
    }

    public void put(T element) throws InterruptedException {

        lock.lock();
        try {

            while(queue.size() == capacity) {
                notFull.await();
            }

            queue.add(element);

            notEmpty.signal();

        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {

        lock.lock();
        try {

            while(queue.isEmpty()) {
                notEmpty.await();
            }

            T element = queue.poll();

            notFull.signal();

            return element;

        } finally {
            lock.unlock();
        }
    }
}

class CustomBlockingQueue {
    public static void main(String[] args) {
        
        CustomBlockingQueueService<Integer> pq = new CustomBlockingQueueService<>(1);
        try {

            Thread putThread = new Thread(() -> {
                for(int ind = 1 ; ind <= 5 ; ind++) try {
                    pq.put(ind);
                    Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        System.getLogger(CustomBlockingQueue.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
            });

            Thread takeThread = new Thread(() -> {
                for(int ind = 1 ; ind <= 5 ; ind++) try {
                    System.out.println(pq.take());
                    } catch (InterruptedException ex) {
                        System.getLogger(CustomBlockingQueue.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
            });

            putThread.start();
            takeThread.start();
        }
        catch(Exception e) {
            System.err.println("exception occured " + e);
        }

    }
}
