import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class UserRequestRateLimit{

    RateLimitSlidingWindow rateLimitSlidingWindow;

    public void UserRequest() {}

    public void rateLimitConfigSetup(int windowSize, int maxRequest) {
        this.rateLimitSlidingWindow = new RateLimitSlidingWindow(windowSize, maxRequest);
    }

    public void makeRequest(int request) {
        if(rateLimitSlidingWindow.handleRequest(request)) {
            System.out.println("user request: " + request + " is accepted");
        }
        else {
            System.out.println("user request: " + request + " is not accepted (rate limited)");
        }
    }
}

class RateLimitSlidingWindow {
    int windowSizeTime;
    int maxRequest;
    Queue<Long> timeWindow;

    public RateLimitSlidingWindow(int windowSize, int maxRequest) {
        this.windowSizeTime = windowSize;
        this.maxRequest = maxRequest;
        timeWindow = new LinkedList<>();
    }

    public synchronized boolean handleRequest(int request) {
        Long currentTime = System.currentTimeMillis();
        this.updateQueue(currentTime);

        if(timeWindow.size() >= maxRequest) {
            return false;
        }
        timeWindow.offer(currentTime);
        return true;
    }

    public void updateQueue(Long currentTime) {
        while(!timeWindow.isEmpty()) {
            Long calculateTime = (currentTime - timeWindow.peek()) / 1000;
            if(calculateTime > windowSizeTime) {
                timeWindow.remove();
            }
            else {
                break;
            }
        }
    }
}

class RateLimiterDesign {
    public static void main(String[] args) {
        UserRequestRateLimit userRequestRateLimit = new UserRequestRateLimit();
        userRequestRateLimit.rateLimitConfigSetup(1, 5);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for(int index = 0 ; index < 10 ; index++) {
            int requestID = index + 1;
            executor.execute(() -> {
                userRequestRateLimit.makeRequest(requestID);

            });
        }

        executor.shutdown();
    }
}
