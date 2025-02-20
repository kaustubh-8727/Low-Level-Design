
/*

1. foodItem -> represents the details of food item in menu.
2. menu -> represents all the foodItems, manages all the crud operations on food item.
3. inventory -> represents all the products needed for cooking, manages all the crud operations on food products.
4. staffPerson -> interface that represents the person who works in restaurent.
5. staffService -> represents all the staff people, manages all crud operation on staff person.
6. staffRole -> enum for staff person role.
7. payment service -> manages the payment through multiple payment methods.
8. order -> represent the order details that customer wants to place.
9. orderService -> manages all the orders placed by the customers.
10. bill -> represents the bill class.
11. order status -> enum.
12. paymentStatus -> enum.

*/


import java.util.ArrayList;
import java.util.List;

enum FoodCategory {
    INDIAN,
    MAXICAN,
    CHINESE,
    THAI
}

enum FoodStatus {
    AVAILABLE,
    NOT_AVAILABLE
}

enum StaffRole {
    CLEANING,
    CHEF,
    WAITER,
    ADMIN,
    BILLING
}

enum StaffStatus {
    AVAILABLE,
    ABSENT,
    ON_LEAVE
}

class FoodItem {
    private String foodId;
    private String name;
    private FoodCategory foodCategory;
    private double price;
    private String description;
    private FoodStatus foodStatus;

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodCategory getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(FoodCategory foodCategory) {
        this.foodCategory = foodCategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FoodStatus getFoodStatus() {
        return foodStatus;
    }

    public void setFoodStatus(FoodStatus foodStatus) {
        this.foodStatus = foodStatus;
    }
}

class Menu {
    private List<FoodItem> menuList = new ArrayList<>();

    public void addFoodItem(FoodItem foodItem) {
        menuList.add(foodItem);
    }

    public void updateFoodItem(String id, FoodItem updatedFoodItem) {
        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i).getFoodId() == null ? id == null : menuList.get(i).getFoodId().equals(id)) {
                menuList.set(i, updatedFoodItem);
                return;
            }
        }
    }

    public void deleteFoodItem(String id) {
        menuList.removeIf(foodItem -> (foodItem.getFoodId() == null ? id == null : foodItem.getFoodId().equals(id)));
    }

    public FoodItem getFoodItem(String id) {
        for (FoodItem foodItem : menuList) {
            if (foodItem.getFoodId() == null ? id == null : foodItem.getFoodId().equals(id)) {
                return foodItem;
            }
        }
        return null;
    }
}

class Ingredient {
    private String ingredientId;
    private String ingredientName;
    private int quantity;

    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

class Inventory {
    private List<Ingredient> ingredientList = new ArrayList<>();

    public void addIngredient(Ingredient ingredient) {
        ingredientList.add(ingredient);
    }

    public void updateIngredient(String ingredientId, Ingredient updatedIngredient) {
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getIngredientId().equals(ingredientId)) {
                ingredientList.set(i, updatedIngredient);
                return;
            }
        }
    }

    public void deleteIngredient(String ingredientId) {
        ingredientList.removeIf(ingredient -> ingredient.getIngredientId().equals(ingredientId));
    }

    public Ingredient getIngredient(String ingredientId) {
        for (Ingredient ingredient : ingredientList) {
            if (ingredient.getIngredientId().equals(ingredientId)) {
                return ingredient;
            }
        }
        return null;
    }
}

abstract class StaffPerson {
    private String personId;
    private String name;
    private StaffRole staffRole;
    private String contactNumber;
    private StaffStatus staffStatus;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StaffRole getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(StaffRole staffRole) {
        this.staffRole = staffRole;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public StaffStatus getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(StaffStatus staffStatus) {
        this.staffStatus = staffStatus;
    }
}

