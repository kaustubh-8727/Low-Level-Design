import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

enum CircuitState {
    CLOSED,
    OPEN,
    HALF_OPEN
}

class CircuitBreakerConfig {

    private final int failureThreshold;
    private final Duration openStateTimeout;
    private final int halfOpenMaxRequests;

    private CircuitBreakerConfig(Builder builder) {
        this.failureThreshold = builder.failureThreshold;
        this.openStateTimeout = builder.openStateTimeout;
        this.halfOpenMaxRequests = builder.halfOpenMaxRequests;
    }

    public int getFailureThreshold() {
        return failureThreshold;
    }

    public Duration getOpenStateTimeout() {
        return openStateTimeout;
    }

    public int getHalfOpenMaxRequests() {
        return halfOpenMaxRequests;
    }

    public static class Builder {

        private int failureThreshold = 5;
        private Duration openStateTimeout = Duration.ofSeconds(30);
        private int halfOpenMaxRequests = 3;

        public Builder failureThreshold(int threshold) {
            if (threshold <= 0) {
                throw new IllegalArgumentException(
                    "Failure threshold must be > 0"
                );
            }

            this.failureThreshold = threshold;
            return this;
        }

        public Builder openStateTimeout(Duration timeout) {
            if (timeout.isNegative() || timeout.isZero()) {
                throw new IllegalArgumentException(
                    "Open state timeout must be > 0"
                );
            }

            this.openStateTimeout = timeout;
            return this;
        }

        public Builder halfOpenMaxRequests(int maxRequests) {
            if (maxRequests <= 0) {
                throw new IllegalArgumentException(
                    "Half-open max requests must be > 0"
                );
            }

            this.halfOpenMaxRequests = maxRequests;
            return this;
        }

        public CircuitBreakerConfig build() {
            return new CircuitBreakerConfig(this);
        }
    }
}

interface FailureTracker {

    void recordSuccess();

    void recordFailure();

    boolean shouldOpen();

    void reset();
}

class ConsecutiveFailureTracker implements FailureTracker {

    private final int failureThreshold;

    private int failureCount;

    public ConsecutiveFailureTracker(int failureThreshold) {
        this.failureThreshold = failureThreshold;
    }

    @Override
    public synchronized void recordSuccess() {
        failureCount = 0;
    }

    @Override
    public synchronized void recordFailure() {
        failureCount++;
    }

    @Override
    public synchronized boolean shouldOpen() {
        return failureCount >= failureThreshold;
    }

    @Override
    public synchronized void reset() {
        failureCount = 0;
    }
}

class CircuitOpenException extends RuntimeException {

    public CircuitOpenException() {
        super("Circuit breaker is OPEN");
    }
}

class CircuitBreakerContext {

    private final CircuitBreakerConfig config;
    private final FailureTracker failureTracker;

    private final AtomicInteger halfOpenRequests = new AtomicInteger(0);

    private final AtomicReference<CircuitState> state = new AtomicReference<>(CircuitState.CLOSED);

    private volatile Instant openedAt;

    public CircuitBreakerContext(
        CircuitBreakerConfig config,
        FailureTracker failureTracker
    ) {
        this.config = config;
        this.failureTracker = failureTracker;
    }

    public CircuitState getState() {
        return state.get();
    }

    public boolean transition(
        CircuitState expected,
        CircuitState next
    ) {
        return state.compareAndSet(expected, next);
    }

    public CircuitBreakerConfig getConfig() {
        return config;
    }

    public FailureTracker getFailureTracker() {
        return failureTracker;
    }

    public Instant getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(Instant openedAt) {
        this.openedAt = openedAt;
    }

    public boolean acquireHalfOpenPermit() {

        while (true) {

            int current = halfOpenRequests.get();

            if (current >= config.getHalfOpenMaxRequests()) {
                return false;
            }

            if (halfOpenRequests.compareAndSet(
                    current,
                    current + 1)) {

                return true;
            }
        }
    }

    public void resetHalfOpenRequests() {
        halfOpenRequests.set(0);
    }
}

interface CircuitStateHandler {

    <T> T execute(
        Supplier<T> action,
        CircuitBreakerContext context
    );
}

class ClosedStateHandler implements CircuitStateHandler {

    @Override
    public <T> T execute(
        Supplier<T> action,
        CircuitBreakerContext context
    ) {

        try {

            T result = action.get();

            context.getFailureTracker().recordSuccess();

            return result;

        } catch (Exception e) {

            context.getFailureTracker()
                   .recordFailure();

            if (context.getFailureTracker().shouldOpen()) {

                boolean transitioned =
                    context.transition(
                        CircuitState.CLOSED,
                        CircuitState.OPEN
                    );

                if (transitioned) {
                    context.setOpenedAt(
                        java.time.Instant.now()
                    );
                }
            }

            throw e;
        }
    }
}

class OpenStateHandler implements CircuitStateHandler {

    private final CircuitStateHandler halfOpenHandler;

    public OpenStateHandler(
        CircuitStateHandler halfOpenHandler
    ) {
        this.halfOpenHandler = halfOpenHandler;
    }

    @Override
    public <T> T execute(
        Supplier<T> action,
        CircuitBreakerContext context
    ) {

        Instant openedAt = context.getOpenedAt();

        boolean timeoutExpired =
            Instant.now().isAfter(
                openedAt.plus(
                    context.getConfig()
                           .getOpenStateTimeout()
                )
            );

        if (!timeoutExpired) {
            throw new CircuitOpenException();
        }

        boolean transitioned =
            context.transition(
                CircuitState.OPEN,
                CircuitState.HALF_OPEN
            );

        if (!transitioned) {
            throw new CircuitOpenException();
        }

        return halfOpenHandler.execute(
            action,
            context
        );
    }
}


class HalfOpenStateHandler
        implements CircuitStateHandler {

    @Override
    public <T> T execute(
        Supplier<T> action,
        CircuitBreakerContext context
    ) {

        if (!context.acquireHalfOpenPermit()) {
            throw new CircuitOpenException();
        }

        try {

            T result = action.get();

            context.transition(
                CircuitState.HALF_OPEN,
                CircuitState.CLOSED
            );

            context.getFailureTracker().reset();

            context.resetHalfOpenRequests();

            return result;

        } catch (Exception e) {

            context.transition(
                CircuitState.HALF_OPEN,
                CircuitState.OPEN
            );

            context.setOpenedAt(
                java.time.Instant.now()
            );

            context.resetHalfOpenRequests();

            throw e;
        }
    }
}

interface CircuitBreaker {

    <T> T execute(Supplier<T> action);
}

class DefaultCircuitBreaker implements CircuitBreaker {

    private final CircuitBreakerContext context;

    private final CircuitStateHandler closedHandler;
    private final CircuitStateHandler openHandler;
    private final CircuitStateHandler halfOpenHandler;

    public DefaultCircuitBreaker(
        CircuitBreakerConfig config
    ) {

        FailureTracker tracker =
            new ConsecutiveFailureTracker(
                config.getFailureThreshold()
            );

        this.context =
            new CircuitBreakerContext(
                config,
                tracker
            );

        this.closedHandler =
            new ClosedStateHandler();

        this.halfOpenHandler =
            new HalfOpenStateHandler();

        this.openHandler =
            new OpenStateHandler(
                this.halfOpenHandler
            );
    }

    @Override
    public <T> T execute(Supplier<T> action) {

        CircuitState state = getCurrentState();

        switch (state) {

            case CLOSED:
                return closedHandler.execute(
                    action,
                    context
                );

            case OPEN:
                return openHandler.execute(
                    action,
                    context
                );

            case HALF_OPEN:
                return halfOpenHandler.execute(
                    action,
                    context
                );

            default:
                throw new IllegalStateException(
                    "Unknown circuit state"
                );
        }
    }

    private CircuitState getCurrentState() {

        return context.getState();
    }
}

class CircuitBreakerSystem {

    public static void main(String[] args) {

        CircuitBreakerConfig config = new CircuitBreakerConfig.Builder()
                .failureThreshold(3)
                .openStateTimeout(Duration.ofSeconds(5))
                .halfOpenMaxRequests(1)
                .build();

        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker(config);

        executeRequest(circuitBreaker, false);
        executeRequest(circuitBreaker, false);
        executeRequest(circuitBreaker, false);
        executeRequest(circuitBreaker, true);

        sleep(6000);

        executeRequest(circuitBreaker, true);
        executeRequest(circuitBreaker, true);
    }

    private static void executeRequest(CircuitBreaker circuitBreaker, boolean success) {

        try {

            String result =
                circuitBreaker.execute(() -> {

                    System.out.println("Calling downstream service...");

                    if (!success) {
                        throw new RuntimeException("Downstream service failed");
                    }

                    return "SUCCESS";
                });

            System.out.println("Response: " + result);

        } catch (CircuitOpenException e) {
            System.out.println("Request rejected: Circuit is OPEN");

        } catch (Exception e) {
            System.out.println("Request failed: " + e.getMessage());
        }

        System.out.println("---------------------------");
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
