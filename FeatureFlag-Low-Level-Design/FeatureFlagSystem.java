import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class UserContext {

    private String userId;
    private String email;
    private String country;

    private Map<String, String> attributes;

    public UserContext(
            String userId,
            String email,
            String country
    ) {
        this.userId = userId;
        this.email = email;
        this.country = country;

        this.attributes = new HashMap<>();

        attributes.put("country", country);
        attributes.put("email", email);
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}


class FeatureFlag {

    private String flagKey;
    private String serviceName;
    private boolean globallyEnabled;
    private Set<String> enabledUsers;
    private int rolloutPercentage;
    private Map<String, String> targetingRules;

    public FeatureFlag(String flagKey, String serviceName) {
        this.flagKey = flagKey;
        this.serviceName = serviceName;
        this.globallyEnabled = false;
        this.enabledUsers = new HashSet<>();
        this.rolloutPercentage = 0;
        this.targetingRules = new HashMap<>();
    }

    public String getFlagKey() {
        return flagKey;
    }

    public boolean isGloballyEnabled() {
        return globallyEnabled;
    }

    public void setGloballyEnabled(boolean globallyEnabled) {
        this.globallyEnabled = globallyEnabled;
    }

    public Set<String> getEnabledUsers() {
        return enabledUsers;
    }

    public int getRolloutPercentage() {
        return rolloutPercentage;
    }

    public void setRolloutPercentage(int rolloutPercentage) {
        this.rolloutPercentage = rolloutPercentage;
    }

    public Map<String, String> getTargetingRules() {
        return targetingRules;
    }
}

interface FeatureFlagStore {

    FeatureFlag getFlag(String flagKey);
    void saveFlag(FeatureFlag featureFlag);
    void deleteFlag(String flagKey);
    List<FeatureFlag> getAllFlags();
}


class InMemoryFeatureFlagStore implements FeatureFlagStore {

    private Map<String, FeatureFlag> store;

    public InMemoryFeatureFlagStore() {
        store = new ConcurrentHashMap<>();
    }

    @Override
    public FeatureFlag getFlag(String flagKey) {
        return store.get(flagKey);
    }

    @Override
    public void saveFlag(FeatureFlag featureFlag ) {
        store.put(featureFlag.getFlagKey(), featureFlag);
    }

    @Override
    public void deleteFlag(String flagKey) {
        store.remove(flagKey);
    }

    @Override
    public List<FeatureFlag> getAllFlags() {
        return new ArrayList<>(store.values());
    }
}

class FeatureFlagService {

    private FeatureFlagStore store;

    public FeatureFlagService(FeatureFlagStore store) {
        this.store = store;
    }

    public void enableGlobally(String flagKey) {
        FeatureFlag flag = validate(flagKey);
        flag.setGloballyEnabled(true);
        store.saveFlag(flag);
    }

    public void disableGlobally(String flagKey) {

        FeatureFlag flag = validate(flagKey);

        flag.setGloballyEnabled(false);

        store.saveFlag(flag);
    }

    public void enableForUser(String flagKey, String userId) {

        FeatureFlag flag = validate(flagKey);
        flag.getEnabledUsers().add(userId);
        store.saveFlag(flag);
    }

    public void disableForUser(String flagKey, String userId) {

        FeatureFlag flag = validate(flagKey);
        flag.getEnabledUsers().remove(userId);
        store.saveFlag(flag);
    }

    public void setRolloutPercentage(String flagKey, int percentage) {

        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }

        FeatureFlag flag = validate(flagKey);
        flag.setRolloutPercentage(percentage);
        store.saveFlag(flag);
    }

    public void addTargetingRule(String flagKey, String key, String value) {

        FeatureFlag flag = validate(flagKey);
        flag.getTargetingRules().put(key, value);
        store.saveFlag(flag);
    }

    public boolean isEnabled(String flagKey, UserContext user) {

        FeatureFlag flag = store.getFlag(flagKey);
        if (flag == null) {
            return false;
        }

        if (flag.isGloballyEnabled()) {
            return true;
        }

        if (flag.getEnabledUsers().contains(user.getUserId())) {
            return true;
        }

        if (!matchesTargetingRules(flag, user)) {
            return false;
        }

        return isInRollout(user.getUserId(), flag.getRolloutPercentage());
    }

    private boolean matchesTargetingRules(FeatureFlag flag, UserContext user) {

        Map<String, String> rules = flag.getTargetingRules();

        if (rules.isEmpty()) {
            return true;
        }

        for (Map.Entry<String, String> entry : rules.entrySet()) {

            String key = entry.getKey();
            String expectedValue = entry.getValue();
            String actualValue = user.getAttributes().get(key);

            if (!expectedValue.equals(actualValue)) {
                return false;
            }
        }

        return true;
    }

    private boolean isInRollout(String userId, int percentage) {
        int hash = Math.abs(userId.hashCode());
        return (hash % 100) < percentage;
    }

    private FeatureFlag validate(String flagKey) {

        FeatureFlag flag = store.getFlag(flagKey);

        if (flag == null) {
            throw new RuntimeException("Feature flag not found : " + flagKey);
        }

        return flag;
    }
}

public class FeatureFlagSystem {

    public static void main(String[] args) {

        FeatureFlagStore store = new InMemoryFeatureFlagStore();
        FeatureFlagService service = new FeatureFlagService(store);

        FeatureFlag checkoutFlag = new FeatureFlag(
                "NEW_CHECKOUT",
                "checkout-service"
        );

        store.saveFlag(checkoutFlag);

        service.enableForUser("NEW_CHECKOUT", "user123");

        service.setRolloutPercentage("NEW_CHECKOUT", 20);

        service.addTargetingRule( "NEW_CHECKOUT", "country", "IN");

        UserContext user1 = new UserContext(
                "user123",
                "abc@gmail.com",
                "IN"
        );

        UserContext user2 = new UserContext(
                "user999",
                "xyz@gmail.com",
                "US"
        );

        System.out.println("User1 Access : " + service.isEnabled("NEW_CHECKOUT", user1));
        System.out.println("User2 Access : " + service.isEnabled("NEW_CHECKOUT", user2));

        service.enableGlobally("NEW_CHECKOUT");

        System.out.println("User2 Access After Global Enable : " + service.isEnabled("NEW_CHECKOUT", user2));
    }
}
