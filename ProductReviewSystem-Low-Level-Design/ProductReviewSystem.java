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
        this.userId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
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
        this.reviewId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
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
    
    public String getProductId() {
        return productId;
    }
    
    public ReviewType getReviewType() {
        return reviewType;
    }
    
    public double getRatingStars() {
        return stars;
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
        List<Review> filteredReviews = new ArrayList<>();
    
        for (Review review : allReviews.values()) {
            boolean matches = false;
    
            // Filter by product ID
            if (reviewFilter.productId != null && review.getProductId().equals(reviewFilter.productId)) {
                matches = true;
            }
    
            // Filter by text (case-insensitive containment)
            if (reviewFilter.text != null && review.comment.toLowerCase().contains(reviewFilter.text.toLowerCase())) {
                matches = true;
            }
    
            // Filter by minimum and maximum rating
            if (reviewFilter.minRating > 0 && review.getRatingStars() >= reviewFilter.minRating) {
                matches = true;
            }
            if (reviewFilter.maxRating > 0 && review.getRatingStars() <= reviewFilter.maxRating) {
                matches = true;
            }
    
            // Filter by review type
            if (reviewFilter.reviewType != null && review.getReviewType() == reviewFilter.reviewType) {
                matches = true;
            }
    
            if (matches) {
                filteredReviews.add(review);
            }
        }
    
        return filteredReviews;
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
        
        int totalReviewCount = 0;
        double averageRating = 0.0;
        int positiveRatingCount = 0;
        int negativeRatingCount = 0;
        int neutralRatingCount = 0;
        double totalRatingStars = 0.0;
        ReviewType concludedReviewType;
        
        for (Map.Entry<String, Review> entry : allReviews.entrySet()) {
            String key = entry.getKey();
            Review review = entry.getValue();
            
            if(review.getProductId() == productId) {
                totalReviewCount++;
                if(review.getReviewType() == ReviewType.POSITIVE) {
                    positiveRatingCount++;
                }
                else if(review.getReviewType() == ReviewType.NEGATIVE) {
                    negativeRatingCount++;
                } else {
                    neutralRatingCount++;
                }
                
                totalRatingStars += review.getRatingStars();
            }
        }
        
        averageRating = totalRatingStars / totalReviewCount;
        if(positiveRatingCount > neutralRatingCount && positiveRatingCount > negativeRatingCount) {
            concludedReviewType = ReviewType.POSITIVE;
        } else if(negativeRatingCount > positiveRatingCount && negativeRatingCount > neutralRatingCount) {
            concludedReviewType = ReviewType.NEGATIVE;
        } else {
            concludedReviewType = ReviewType.NEUTRAL;
        }
        
        ReviewSummary reviewSummary = new ReviewSummary(totalReviewCount, averageRating, concludedReviewType);

        return reviewSummary;
    }
    
    public void displayReviewSummary(String productId) {
        ReviewSummary reviewSummary = this.getReviewSummary(productId);
        
        if (reviewSummary != null) {
            System.out.println("Review Summary for Product ID: " + productId);
            System.out.println("Total Reviews: " + reviewSummary.reviewCount);
            System.out.println("Average Rating: " + reviewSummary.reviewAverageStars);
            System.out.println("Concluded Review Type: " + reviewSummary.concludedReviewType);
            System.out.println("------------------------------\n");
        } else {
            System.out.println("No reviews found for Product ID: " + productId);
            System.out.println("------------------------------\n");
        }
    }

}

class Product {
    String productId;
    String productName;
    double price;
    String productType;
    String description;
    
    public Product(String productName, String productType, String description, double price) {
        this.productId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
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
        this.filterId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.productId = productId;
        this.text = text;
        this.minRating = minRating;
        this.maxRating = maxRating;
        this.reviewType = reviewType;
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
		
		// user5 wants to add review to product2
		userId = user5.getUserId();
		productId = product2.getProductId();
		reviewService.addReview(productId, userId, "good product", 4.5, ReviewType.POSITIVE);
		
		// display review summary
		reviewService.displayReviewSummary(productId);
		
		// user1 fetches reviews based on filters 
		ReviewFilter reviewFilter = new ReviewFilter(null, null, 0, 0, ReviewType.NEUTRAL);
		List<Review> reviewFetchedResult = reviewService.getReviews(reviewFilter);

        for (Review review : reviewFetchedResult) {
            System.out.println("Review Details:");
            System.out.println("Review ID: " + review.getReviewId());
            System.out.println("Product ID: " + review.getProductId());
            System.out.println("User ID: " + review.userId);
            System.out.println("Comment: " + review.comment);
            System.out.println("Stars: " + review.stars);
            System.out.println("Review Type: " + review.reviewType);
            System.out.println("Review Timestamp: " + review.reviewTimestamp);
            System.out.println("------------------------------\n");
        }
	}
}
