/*

product review system

1. user
2. user management
3. review
4. review summary
5. review service
6. product
7. product service
8. review type enum
9. review filter

*/

import java.util.*;
import java.util.Date;

enum ReviewType {
    POSITIVE,
    NEGATIVE,
    NEUTRAL
}

class User {
    String userId;
    String userName;
    String email;
    String contactNumber;
    
    public User(String userName, String email, String contactNumber) {
        this.userId = "1234";
        this.userName = userName;
        this.email = email;
        this.contactNumber = contactNumber;
    }
    
    public String getUserId() {
        return userId;
    }

}

class UserManagementService {
    
    private static UserManagementService instance;
    
    List<User> allUsers = new ArrayList<>();
    
    public static synchronized UserManagementService getInstance() {
        if (instance == null) {
            instance = new UserManagementService();
        }
        return instance;
    }
    
    public void createUser(User user) {
        allUsers.add(user);
    }
    
    public void removeUser(User user) {
        allUsers.remove(user);
    }
    
    public User getUser(String userId) {
        for(User user : allUsers) {
            if(user.userId == userId) {
                return user;
            }
        }
        return null;
    }
}

class Review {
    
    String reviewId;
    String productId;
    String userId;
    String comment;
    double stars;
    ReviewType reviewType;
    Date reviewTimestamp;
    
    public Review(String productId, String userId, String comment, double stars, ReviewType reviewType) {
        this.reviewId = "1234";
        this.productId = productId;
        this.userId = userId;
        this.comment = comment;
        this.stars = stars;
        this.reviewType = reviewType;
        this.reviewTimestamp = new Date();
    }
    
    public String getReviewId() {
        return reviewId;
    }
}

class ReviewSummary {
    int reviewCount;
    double reviewAverageStars;
    ReviewType concludedReviewType;
    
    public ReviewSummary(int reviewCount, double reviewAverageStars, ReviewType concludedReviewType) {
        this.reviewCount = reviewCount;
        this.reviewAverageStars = reviewAverageStars;
        this.concludedReviewType = concludedReviewType;
    }
}

class ReviewService {
    
    private static ReviewService instance;
    
    public static synchronized ReviewService getInstance() {
        if (instance == null) {
            instance = new ReviewService();
        }
        return instance;
    }
    
    Map<String, Review> allReviews = new HashMap<>();
    
    public void addReview(String productId, String userId, String comment, double stars, ReviewType reviewType) {
        Review review = new Review(productId, userId, comment, stars, reviewType);
        allReviews.put(review.getReviewId(), review);
    }
    
    public List<Review> getReviews(ReviewFilter reviewFilter) {
        // logic to fetch all the reviews based on review filter
        return null;
    }
    
    public void removeReview(String reviewId) {
        if(allReviews.containsKey(reviewId)) {
            allReviews.remove(reviewId);
        }
    }
    
    public void updateReview(Review review) {
        String reviewId = review.getReviewId();
        
        if(allReviews.containsKey(reviewId)) {
            allReviews.put(reviewId, review);
        }
    }
    
    public ReviewSummary getReviewSummary(String productId) {
        // return review summary 
        return null;
    }
}

class Product {
    String productId;
    String productName;
    double price;
    String productType;
    String description;
    
    public Product(String productName, String productType, String description, double price) {
        this.productName = productName;
        this.productType = productType;
        this.description = description;
        this.price = price;
    }
    
    public String getProductId() {
        return productId;
    }
}

class ProductService {
    
    private static ProductService instance;
    
    public static synchronized ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }
    
    List<Product> allProducts = new ArrayList<>();
    
    public void addProduct(Product product) {
        allProducts.add(product);
    }
    
    public void removeProduct(String productId) {
        Product product = getProductById(productId);
        allProducts.remove(product);
    }
    
    public Product getProductById(String productId) {
        for(Product product : allProducts) {
            if(product.getProductId() == productId) {
                return product;
            }
        }
        
        return null;
    }
}

class ReviewFilter {
    String filterId;
    String productId;
    String text;
    double minRating;
    double maxRating;
    ReviewType reviewType;
    
    
    public ReviewFilter(String productId, String text, double minRating, double maxRating, ReviewType reviewType) {
        this.filterId = "1234";
        this.productId = productId;
        this.text = text;
        this.minRating = minRating;
        this.maxRating = maxRating;
    }
}

class ServiceFactory {

    private ServiceFactory() {}

    public static UserManagementService getUserManagementService() {
        return UserManagementService.getInstance();
    }

    public static ReviewService getReviewService() {
        return ReviewService.getInstance();
    }

    public static ProductService getProductService() {
        return ProductService.getInstance();
    }
}

public class Main {
	public static void main(String[] args) {
	    
	    // get all services
	    UserManagementService userManagementService = ServiceFactory.getUserManagementService();
	    ReviewService reviewService = ServiceFactory.getReviewService();
	    ProductService productService = ServiceFactory.getProductService();
		
		// create user
		User user1 = new User("jack1", "jack1@gmail.com", "98785745634");
		User user2 = new User("jack2", "jack2@gmail.com", "98785745634");
		User user3 = new User("jack3", "jack3@gmail.com", "98785745634");
		User user4 = new User("jack4", "jack4@gmail.com", "98785745634");
		User user5 = new User("jack5", "jack5@gmail.com", "98785745634");
		userManagementService.createUser(user1);
		userManagementService.createUser(user2);
		userManagementService.createUser(user3);
		userManagementService.createUser(user4);
		userManagementService.createUser(user5);
		
		// create product
		Product product1 = new Product("iPhone", "mobile", "iphone 15 pro", 150000);
		Product product2 = new Product("blanket", "mobile", "iphone 15 pro", 150000);
		productService.addProduct(product1);
		productService.addProduct(product2);
		
		// user1 wants to add review to product1
		String userId = user1.getUserId();
		String productId = product1.getProductId();
		reviewService.addReview(productId, userId, "best phone ever", 4.5, ReviewType.POSITIVE);
		
		// user2 wants to add review to product1
		userId = user2.getUserId();
		productId = product1.getProductId();
		reviewService.addReview(productId, userId, "good phone", 4.0, ReviewType.POSITIVE);
		
		// user3 wants to add review to product1
		userId = user3.getUserId();
		productId = product1.getProductId();
		reviewService.addReview(productId, userId, "too slow", 2.5, ReviewType.NEGATIVE);
		
		// user4 wants to add review to product1
		userId = user4.getUserId();
		productId = product1.getProductId();
		reviewService.addReview(productId, userId, "just like normal phone", 3.0, ReviewType.NEUTRAL);
		
		// user5 wants to add review to product1
		userId = user5.getUserId();
		productId = product1.getProductId();
		reviewService.addReview(productId, userId, "amazing features", 4.5, ReviewType.POSITIVE);
		
	}
}
