
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

enum MembershipTier {
    BASIC,
    PREMIUM,
    PREMIUM_PLUS
}

enum SubscriptionStatus {
    ACTIVE,
    EXPIRED,
    CANCELLED,
    PENDING_PAYMENT
}

enum PaymentStatus {
    SUCCESS,
    FAILED,
    PENDING
}

class User {
    private final String id;
    private String name;
    private String email;
    private String phone;

    public User(String name, String email, String phone) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
}

class Service {
    private String id;
    private String name;
    private List<MembershipPlan> availablePlans;

    public Service(String id, String name, List<MembershipPlan> plans) {
        this.id = id;
        this.name = name;
        this.availablePlans = plans;
    }

    public List<MembershipPlan> getAvailablePlans() {
        return availablePlans;
    }
}

class MembershipPlan {
    private String id;
    private MembershipTier tier;
    private double price;
    private List<String> benefits;
    private Period validity;

    public MembershipPlan(String id, MembershipTier tier,
                      double price, Period validity,
                      List<String> benefits) {
        this.id = id;
        this.tier = tier;
        this.price = price;
        this.validity = validity;
        this.benefits = benefits;
    }

    public double getPrice() { return price; }
    public Period getValidity() { return validity; }
    public MembershipTier getTier() { return tier; }
}

class Subscription {
    private String id;
    private User user;
    private MembershipPlan plan;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private SubscriptionStatus status;

    public Subscription(String id, User user, MembershipPlan plan) {
        this.id = id;
        this.user = user;
        this.plan = plan;
        this.startDate = LocalDate.now();
        this.expiryDate = startDate.plus(plan.getValidity());
        this.status = SubscriptionStatus.PENDING_PAYMENT;
    }

    public void activate() {
        this.status = SubscriptionStatus.ACTIVE;
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public User getUser() { return user; }
    public MembershipPlan getPlan() { return plan; }
}

interface PaymentMethod {
    PaymentResult pay(double amount);
}

class PaymentResult {
    private PaymentStatus status;

    public PaymentResult(PaymentStatus status) {
        this.status = status;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}

class CardPayment implements PaymentMethod {

    @Override
    public PaymentResult pay(double amount) {
        System.out.println("Processing Card Payment: " + amount);
        return new PaymentResult(PaymentStatus.SUCCESS);
    }
}

class UpiPayment implements PaymentMethod {

    @Override
    public PaymentResult pay(double amount) {
        System.out.println("Processing UPI Payment: " + amount);
        return new PaymentResult(PaymentStatus.SUCCESS);
    }
}

interface PricingStrategy {
    double calculatePrice(User user, MembershipPlan plan);
}

class DefaultPricingStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(User user, MembershipPlan plan) {
        return plan.getPrice();
    }
}

class FestivalDiscountStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(User user, MembershipPlan plan) {
        return plan.getPrice() * 0.9; // 10% discount
    }
}

interface NotificationService {
    void send(User user, String message);
}

class EmailNotificationService implements NotificationService {

    @Override
    public void send(User user, String message) {
        System.out.println("Sending EMAIL to " + user.getEmail() + ": " + message);
    }
}

class SmsNotificationService implements NotificationService {

    @Override
    public void send(User user, String message) {
        System.out.println("Sending SMS to " + user.getName() + ": " + message);
    }
}

class SubscriptionService {

    private PricingStrategy pricingStrategy;
    private NotificationService notificationService;

    public SubscriptionService(PricingStrategy pricingStrategy,
                               NotificationService notificationService) {
        this.pricingStrategy = pricingStrategy;
        this.notificationService = notificationService;
    }

    public Subscription buySubscription(User user,
                                        MembershipPlan plan,
                                        PaymentMethod paymentMethod) {

        double finalAmount = pricingStrategy.calculatePrice(user, plan);

        PaymentResult result = paymentMethod.pay(finalAmount);

        Subscription subscription =
                new Subscription(UUID.randomUUID().toString(), user, plan);

        if (result.getStatus() == PaymentStatus.SUCCESS) {
            subscription.activate();
            notificationService.send(user, "Subscription activated!");
        } else {
            notificationService.send(user, "Payment failed!");
        }

        return subscription;
    }

    public void cancelSubscription(Subscription subscription) {
        subscription.cancel();
        notificationService.send(subscription.getUser(),
                "Subscription cancelled.");
    }

    public void renewSubscription(Subscription subscription,
                                  PaymentMethod paymentMethod) {
        buySubscription(subscription.getUser(),
                subscription.getPlan(),
                paymentMethod);
    }
}

class SubscriptionLowLevelDesign {

    public static void main(String[] args) {

        MembershipPlan premiumPlan = new MembershipPlan(
                "plan1",
                MembershipTier.PREMIUM,
                999,
                Period.ofDays(30),
                List.of("HD Streaming", "No Ads")
        );

        User user = new User("Kaustubh", "kaustubh@email.com", "9999999999");

        SubscriptionService service =
                new SubscriptionService(
                        new DefaultPricingStrategy(),
                        new EmailNotificationService()
                );

        Subscription subscription = service.buySubscription(
                user,
                premiumPlan,
                new CardPayment()
        );
    }
}
