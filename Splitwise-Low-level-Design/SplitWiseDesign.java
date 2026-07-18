import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

enum SplitType {
    EQUAL,
    PERCENTAGE
}

class User {
    String username;
    String userId;
}

class Split {
    User person;
    double amount;

    public Split(User person, double amount) {
        this.person = person;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}

class Expense {
    private String id;
    private User paidBy;
    private List<Split> splits = new ArrayList<>();
    private String description;
    private double amount;
    private SplitType splitType;

    public Expense(User paidBy, List<Split> splits, String description, double amount, SplitType splitType) {
        this.id = UUID.randomUUID().toString();
        this.splits = splits;
        this.description = description;
        this.amount = amount;
        this.splitType = splitType;
        this.paidBy = paidBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(User paidBy) {
        this.paidBy = paidBy;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public void setSplits(List<Split> splits) {
        this.splits = splits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public void setSplitType(SplitType splitType) {
        this.splitType = splitType;
    }
}

class BalanceSheet {
    double totalExpense = 0.0;
    double totalAmountPaid = 0.0;
    Map<User, Double> balances = new HashMap<>();

    public void addTotalExpense(double amount) {
        this.totalExpense += amount;
    }

    public void addTotalAmountPaid(double amount) {
        this.totalAmountPaid += amount;
    }

    public void addBalance(User user, double amount) {
        balances.put(user, balances.getOrDefault(user, 0.0) + amount);
    }

    public void clearBalance() {
        balances.clear();
    }
}

class Group {
    private String groupId;
    private String groupName;
    private List<User> members = new ArrayList<>();
    private List<Expense> expense = new ArrayList<>();
    private Map<User, BalanceSheet> userBalanceSheet = new HashMap<>();

    public Group(String groupName, List<User> members) {
        this.groupId = UUID.randomUUID().toString();
        this.groupName = groupName;
        this.members = members;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<Expense> getExpense() {
        return expense;
    }

    public void setExpense(List<Expense> expense) {
        this.expense = expense;
    }

    public Map<User, BalanceSheet> getUserBalanceSheet() {
        return userBalanceSheet;
    }

    public void setUserBalanceSheet(Map<User, BalanceSheet> userBalanceSheet) {
        this.userBalanceSheet = userBalanceSheet;
    }

    public void addExpense(Expense expense) {
        this.expense.add(expense);
    }
}

interface ISplitExpense {
    public List<Split> splitExpense(List<User> members, double amount, Map<User, Double> metadata); 
}

class SplitExpenseEqual implements ISplitExpense {

    public List<Split> splitExpense(List<User> members, double amount, Map<User, Double> metadata) {
        List<Split> splits = new ArrayList<>();
        int totalMembers = members.size();
        double equalAmount = amount / totalMembers;

        for(User user : members) {
            splits.add(new Split(user, equalAmount));
        }

        return splits;
    }
}

class SplitExpensePercentage implements ISplitExpense {

    public List<Split> splitExpense(List<User> members, double amount, Map<User, Double> metadata) {
        List<Split> splits = new ArrayList<>();
        int totalMembers = members.size();

        for(User user : members) {

            double userAmount = amount * (metadata.get(user) / 100);
            splits.add(new Split(user, userAmount));
        }

        return splits;
    }
}

class SplitExpenseFactory {

    public ISplitExpense getSplitStrategy(SplitType splitType) {
        if(splitType == SplitType.EQUAL) {
        return new SplitExpenseEqual();
    }

    if(splitType == SplitType.PERCENTAGE) {
        return new SplitExpensePercentage();
    }

        return null;
    }
}

class ExpenseService {

    SplitExpenseFactory splitExpenseFactory;
    BalanceSheetService balanceSheetService;

    public ExpenseService(SplitExpenseFactory splitExpenseFactory, BalanceSheetService balanceSheetService) {
        this.splitExpenseFactory = splitExpenseFactory;
        this.balanceSheetService = balanceSheetService;
    }

    public void createExpense(Group group, double amount, User paidBy, List<User> members, SplitType splitType, Map<User, Double> metadata, String description) {

        List<Split> splits = this.splitExpenseFactory.getSplitStrategy(splitType).splitExpense(members, amount, metadata);
        Expense expense = new Expense(paidBy, splits, description, amount, splitType);
        group.addExpense(expense);

        balanceSheetService.updateBalanceSheet(group, paidBy, splits);
    }
}

class BalanceSheetService {

    public BalanceSheet getBalanceSheet(Group group, User user) {

        if(!group.getUserBalanceSheet().containsKey(user)) {
            group.getUserBalanceSheet().put(user, new BalanceSheet());
        }

        return group.getUserBalanceSheet().get(user);
    }

    public void updateBalanceSheet(Group group, User paidBy, List<Split> splits) {

        double totalAmount = splits.stream().mapToDouble(Split::getAmount).sum();

        getBalanceSheet(group, paidBy).totalAmountPaid += totalAmount;

        for(Split split : splits) {

            User user = split.person;
            double amount = split.amount;

            getBalanceSheet(group, user).totalExpense += amount;

            if(!user.userId.equals(paidBy.userId)) {
                getBalanceSheet(group, user).addBalance(paidBy, -amount);
                getBalanceSheet(group, paidBy).addBalance(user, amount);
            }

        }

    }
}

class BalanceSheetSimplify {

    public void SimplifyBalanceSheet(Group group) {

        Map<User, Double> userTrxnMap = new HashMap<>();

        for (User user : group.getMembers()) {

            BalanceSheet sheet = group.getUserBalanceSheet().get(user);

            if (sheet == null) continue;

            double sum = 0;

            for (double amount : sheet.balances.values()) sum += amount;

            userTrxnMap.put(user, sum);

            sheet.clearBalance();
        }

        Map<User, Double> positiveTrxn = new HashMap<>();
        Map<User, Double> negativeTrxn = new HashMap<>();

        for (Map.Entry<User, Double> entry : userTrxnMap.entrySet()) {

            if (entry.getValue() > 0.00001) {
                positiveTrxn.put(entry.getKey(), entry.getValue());
            } else if (entry.getValue() < -0.00001) {
                negativeTrxn.put(entry.getKey(), entry.getValue());
            }
        }

        List<Map.Entry<User, Double>> creditors = new ArrayList<>(positiveTrxn.entrySet());

        List<Map.Entry<User, Double>> debtors = new ArrayList<>(negativeTrxn.entrySet());

        int i = 0;
        int j = 0;

        while (i < creditors.size() && j < debtors.size()) {

            Map.Entry<User, Double> creditor = creditors.get(i);
            Map.Entry<User, Double> debtor = debtors.get(j);

            double amount = Math.min(
                    creditor.getValue(),
                    -debtor.getValue()
            );

            group.getUserBalanceSheet().get(creditor.getKey()).addBalance(debtor.getKey(), amount);

            group.getUserBalanceSheet().get(debtor.getKey()).addBalance(creditor.getKey(), -amount);

            creditor.setValue(creditor.getValue() - amount);
            debtor.setValue(debtor.getValue() + amount);

            if (Math.abs(creditor.getValue()) < 0.00001) {
                i++;
            }

            if (Math.abs(debtor.getValue()) < 0.00001) {
                j++;
            }
        }
    }
}

class GroupService {

    ExpenseService expenseService;
    BalanceSheetSimplify balanceSheetSimplify;
    Map<String, Group> groups = new HashMap<>();

    public GroupService(ExpenseService expenseService, BalanceSheetSimplify balanceSheetSimplify) {
        this.expenseService = expenseService;
        this.balanceSheetSimplify = balanceSheetSimplify;
    }

    public void createGroup(String groupName, List<User> members) {
        if(!groups.containsKey(groupName)) {
            Group group = new Group(groupName, members);
            groups.put(groupName, group);
        }
    }

    public void addExpense(String groupName, double amount, User paidBy, SplitType splitType, Map<User, Double> metadata, String description) {

        if(!groups.containsKey(groupName)) return;
        Group group = groups.get(groupName);

        expenseService.createExpense(group, amount, paidBy, group.getMembers(), splitType, metadata, description);
    }

    public void simplifyDebt(String groupName) {

        if(!groups.containsKey(groupName)) return;
        Group group = groups.get(groupName);

        balanceSheetSimplify.SimplifyBalanceSheet(group);
    }

    public void printBalanceSheet(String groupName) {

        if(!groups.containsKey(groupName)) {
            return;
        }

        Group group = groups.get(groupName);

        for(User user : group.getMembers()) {

            BalanceSheet sheet = group.getUserBalanceSheet().get(user);

            if(sheet == null) continue;

            System.out.println("--------------------------------");

            System.out.println("User : " + user.username);
            System.out.println("Total Paid : " + sheet.totalAmountPaid);
            System.out.println("Total Expense : " + sheet.totalExpense);
            System.out.println("Balances :");

            for(Map.Entry<User, Double> entry : sheet.balances.entrySet()) {

                if(Math.abs(entry.getValue()) < 0.0001) continue;

                if(entry.getValue() > 0) {
                    System.out.println(entry.getKey().username + " owes " + user.username + " : " + entry.getValue());
                } else {
                    System.out.println(user.username + " owes " + entry.getKey().username + " : " + (-entry.getValue()));
                }
            }
        }
    }
}

class SplitWiseDesign {
    public static void main(String[] args) {

        User alice = new User();
        alice.userId = "1";
        alice.username = "Alice";

        User bob = new User();
        bob.userId = "2";
        bob.username = "Bob";

        User charlie = new User();
        charlie.userId = "3";
        charlie.username = "Charlie";

        List<User> members = new ArrayList<>();
        members.add(alice);
        members.add(bob);
        members.add(charlie);

        SplitExpenseFactory splitExpenseFactory = new SplitExpenseFactory();
        BalanceSheetService balanceSheetService = new BalanceSheetService();
        ExpenseService expenseService = new ExpenseService(splitExpenseFactory, balanceSheetService);

        BalanceSheetSimplify balanceSheetSimplify = new BalanceSheetSimplify();

        GroupService groupService = new GroupService(expenseService, balanceSheetSimplify);

        groupService.createGroup("Trip", members);

        groupService.addExpense(
                "Trip",
                300,
                alice,
                SplitType.EQUAL,
                new HashMap<>(),
                "Dinner"
        );

        groupService.addExpense(
                "Trip",
                150,
                bob,
                SplitType.EQUAL,
                new HashMap<>(),
                "Taxi"
        );

        System.out.println("========= BEFORE SIMPLIFICATION =========");
        groupService.printBalanceSheet("Trip");

        groupService.simplifyDebt("Trip");

        System.out.println("\n========= AFTER SIMPLIFICATION =========");
        groupService.printBalanceSheet("Trip");
    }
}
