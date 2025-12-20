import java.time.LocalDateTime;
import java.util.*;
/*

1. discount context
2. discount rule
3. discount action
4. discount
5. discount engine

*/

class DiscountContext {
    User user;
    Cart cart;
    double cartTotal;
    LocalDateTime orderTime;
    String couponCode;

    public DiscountContext(User user, Cart cart, double cartTotal, String couponCode) {
        this.user = user;
        this.cart = cart;
        this.cartTotal = cartTotal;
        this.couponCode = couponCode;
        this.orderTime = LocalDateTime.now();
    }
}

interface DiscountRule {
    boolean isApplicable(DiscountContext context);
}

class MinCartValueRule implements DiscountRule {
    private double minAmount;

    public MinCartValueRule(double minAmount) {
        this.minAmount = minAmount;
    }

    public boolean isApplicable(DiscountContext ctx) {
        return ctx.cartTotal >= minAmount;
    }
}

class CouponRule implements DiscountRule {
    private String validCoupon;

    public CouponRule(String validCoupon) {
        this.validCoupon = validCoupon;
    }

    public boolean isApplicable(DiscountContext ctx) {
        return validCoupon.equals(ctx.couponCode);
    }
}


interface DiscountAction {
    double apply(double amount, DiscountContext context);
}

class PercentageOffAction implements DiscountAction {
    private double percentage;

    public PercentageOffAction(double percentage) {
        this.percentage = percentage;
    }

    public double apply(double amount, DiscountContext ctx) {
        return amount - (amount * percentage / 100);
    }
}

class FlatOffAction implements DiscountAction {
    private double flatAmount;

    public FlatOffAction(double flatAmount) {
        this.flatAmount = flatAmount;
    }

    public double apply(double amount, DiscountContext ctx) {
        return amount - flatAmount;
    }
}

class Discount {
    private DiscountRule rule;
    private DiscountAction action;
    private int priority;
    private DiscountType type;

    public Discount(DiscountRule rule, DiscountAction action, int priority, DiscountType type) {
        this.rule = rule;
        this.action = action;
        this.priority = priority;
        this.type = type;
    }

    public DiscountRule getRule() {
        return rule;
    }

    public DiscountAction getAction() {
        return action;
    }

    public int getPriority() {
        return priority;
    }

    public DiscountType getType() {
        return type;
    }
}


enum DiscountType {
    ITEM,
    CART,
    ORDER
}

class DiscountEngine {

    private List<Discount> discounts;

    public DiscountEngine(List<Discount> discounts) {
        this.discounts = discounts;
    }

    public double applyDiscounts(DiscountContext context) {

        double amount = context.cartTotal;

        List<Discount> applicableDiscounts = discounts.stream()
                .filter(d -> d.getRule().isApplicable(context))
                .sorted(Comparator.comparingInt(Discount::getPriority))
                .toList();

        for (Discount discount : applicableDiscounts) {
            amount = discount.getAction().apply(amount, context);
        }

        return Math.max(amount, 0);
    }
}

public class DiscountManagementLLD {

    public static void main(String[] args) {

        // 1. Create user
        User user = new User("U1", "Kaustubh");

        // 2. Create products
        Product laptop = new Product("P1", "Laptop", 60000);
        Product mouse = new Product("P2", "Mouse", 2000);

        // 3. Create cart and add items
        Cart cart = new Cart();
        cart.addItem(laptop, 1);
        cart.addItem(mouse, 2);

        user.setCart(cart);

        double cartTotal = cart.getTotalAmount();
        System.out.println("Cart Total: " + cartTotal);

        // 4. Create discount rules
        DiscountRule minCartRule = new MinCartValueRule(5000);
        DiscountRule couponRule = new CouponRule("NEWUSER10");

        // 5. Create discount actions
        DiscountAction tenPercentOff = new PercentageOffAction(10);
        DiscountAction flat500Off = new FlatOffAction(500);

        // 6. Create discounts
        Discount festivalDiscount = new Discount(
                minCartRule,
                tenPercentOff,
                1,
                DiscountType.CART
        );

        Discount couponDiscount = new Discount(
                couponRule,
                flat500Off,
                2,
                DiscountType.ORDER
        );

        // 7. Discount Engine
        DiscountEngine discountEngine = new DiscountEngine(
                List.of(festivalDiscount, couponDiscount)
        );

        // 8. Create discount context
        DiscountContext context = new DiscountContext(
                user,
                cart,
                cartTotal,
                "NEWUSER10"
        );

        // 9. Apply discounts
        double finalAmount = discountEngine.applyDiscounts(context);

        System.out.println("Final Payable Amount: " + finalAmount);
    }
}
