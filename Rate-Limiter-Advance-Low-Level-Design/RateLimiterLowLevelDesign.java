import java.util.concurrent.ConcurrentHashMap;

enum AlgorithmType {
    SLIDING_WINDOW,
    TOKEN_BUCKET
}

class RateLimiterConfig {

    private AlgorithmType algorithmType;

    // Sliding window config
    private long maxTokens;
    private long windowDuration;

    // Token bucket config
    private long bucketSize;
    private long bucketRefillDuration;

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }

    public long getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(long maxTokens) {
        this.maxTokens = maxTokens;
    }

    public long getWindowDuration() {
        return windowDuration;
    }

    public void setWindowDuration(long windowDuration) {
        this.windowDuration = windowDuration;
    }

    public long getBucketSize() {
        return bucketSize;
    }

    public void setBucketSize(long bucketSize) {
        this.bucketSize = bucketSize;
    }

    public long getBucketRefillDuration() {
        return bucketRefillDuration;
    }

    public void setBucketRefillDuration(long bucketRefillDuration) {
        this.bucketRefillDuration = bucketRefillDuration;
    }
}


class RateLimitKey {
    private final String key;

    public RateLimitKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateLimitKey that = (RateLimitKey) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}


interface RateLimitStore {

    <T> T getRateLimitState(RateLimitKey key);
    <T> void updateState(RateLimitKey key, T state);
}

class InMemoryRateLimitStore implements RateLimitStore {

    ConcurrentHashMap<RateLimitKey, Object> store = new ConcurrentHashMap<>();

    public <T> T getRateLimitState(RateLimitKey key) {
        return (T) store.get(key);
    }

    public <T> void updateState(RateLimitKey key, T state) {
        store.put(key, state);
    }

}

interface RateLimiterAlgorithm {
    boolean handleRequest(RateLimitKey rateLimitKey);
}

class TokenBucketAlgorithm implements RateLimiterAlgorithm {

    static class TokenBucketState {
        long tokens;
        long lastRefillTime;
    }

    private final RateLimitStore store;
    private final RateLimiterConfig config;

    public TokenBucketAlgorithm(RateLimitStore store, RateLimiterConfig config) {
        this.store = store;
        this.config = config;
    }

    @Override
    public boolean handleRequest(RateLimitKey key) {

        TokenBucketState state = store.getRateLimitState(key);
        if (state == null) {
            synchronized (this) {
                state = store.getRateLimitState(key);
                if (state == null) {
                    state = createInitialState();
                    store.updateState(key, state);
                }
            }
        }

        synchronized (state) {
            long now = System.currentTimeMillis();
            long elapsed = now - state.lastRefillTime;

            if (elapsed >= config.getBucketRefillDuration()) {
                long tokensToAdd = elapsed / config.getBucketRefillDuration();
                state.tokens = Math.min(
                    config.getBucketSize(),
                    state.tokens + tokensToAdd
                );
                state.lastRefillTime += tokensToAdd * config.getBucketRefillDuration();
            }

            if (state.tokens > 0) {
                state.tokens--;
                return true;
            }

            return false;
        }
    }

    private TokenBucketState createInitialState() {
        TokenBucketState state = new TokenBucketState();
        state.tokens = config.getBucketSize();
        state.lastRefillTime = System.currentTimeMillis();
        return state;
    }

    private void refillState(TokenBucketState state) {
        long now = System.currentTimeMillis();
        long elapsed = now - state.lastRefillTime;

        long tokensToAdd = elapsed / config.getBucketRefillDuration();

        if (tokensToAdd > 0) {
            state.tokens = Math.min(
                config.getBucketSize(),
                state.tokens + tokensToAdd
            );
            state.lastRefillTime += tokensToAdd * config.getBucketRefillDuration();
        }
    }
}

class UserRequestRateLimiter {

    private static volatile UserRequestRateLimiter instance;
    RateLimiterAlgorithm rateLimit;

    private UserRequestRateLimiter() {}

    public static UserRequestRateLimiter getInstance() {
        if (instance == null) {
            synchronized (UserRequestRateLimiter.class) {
                if (instance == null) {
                    instance = new UserRequestRateLimiter();
                }
            }
        }
        return instance;
    }

    public void intializeConfig() {
        RateLimiterConfig rateLimiterConfig = new RateLimiterConfig();
        rateLimiterConfig.setAlgorithmType(AlgorithmType.TOKEN_BUCKET);
        rateLimiterConfig.setBucketSize(2);
        rateLimiterConfig.setBucketRefillDuration(5000); // milliseconds

        RateLimitStore store = new InMemoryRateLimitStore();
        rateLimit = new TokenBucketAlgorithm(store, rateLimiterConfig);
    }
    
    public void makeRequest(String key) {
        RateLimitKey rateLimitKey = new RateLimitKey(key);
        boolean result = rateLimit.handleRequest(rateLimitKey);
        if(result == true) {
            System.out.println("user request with key = " + rateLimitKey.getKey() + " handeled successfully");
        }
        else {
            System.out.println("user request with key " + rateLimitKey.getKey() + " rejected");
        }
    }
}

class RateLimiterLowLevelDesign {
    public static void main(String[] args) {

        UserRequestRateLimiter instance = UserRequestRateLimiter.getInstance();
        instance.intializeConfig();

        for(int ind = 0 ; ind < 20 ; ind++) {
            if(ind % 2 == 0) {
                new Thread(() -> {
                    instance.makeRequest("user_1");
                }).start();
            }
            else {
                new Thread(() -> {
                    instance.makeRequest("user_2");
                }).start();
            }
        }
    }
}
