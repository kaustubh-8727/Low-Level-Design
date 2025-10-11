# 🧠 StackOverflow - Low Level Design (LLD)

This repository contains the Low-Level Design (LLD) of a simplified **StackOverflow** system.  
The design captures how users can create questions, submit answers, comment, view feeds, and interact through votes — with extensible and modular service architecture.

---

## 🚀 Functional Requirements

1. Users can **register/login** and create profiles.  
2. Users can **post questions** or discussions.  
3. Users can **submit answers** to questions.  
4. Users can **comment** on questions or answers.  
5. Users can **upvote/downvote** posts and comments.  
6. Users can **view personalized feeds** of questions.  
7. Users can **search** questions by tags or keywords.  
8. Users can **earn reputation badges** based on activity (optional).

---

## 🧩 Core Entities

### 👤 User
Represents a StackOverflow user with profile and post associations.

```java
class User {
    String userId;
    String name;
    String email;
    String passwordHash;
    Profile profile;
    List<Post> posts;
}
```

### Profile
Contains user-specific details such as interests, badges, and reputation.

```java
Copy code
class Profile {
    String userId;
    String username;
    String bio;
    List<String> techInterests;
    int reputationScore;
    List<Badge> badges;
}
```

### Post
Represents both questions and answers using a unified model.
Answers reference their parent question via parentPostId.

```java
class Post {
    String postId;
    String userId;
    String title;
    String description;
    PostType type; // QUESTION or ANSWER
    String parentPostId; // null for question
    List<String> tags;
    List<Comment> comments;
    int upvotes;
    int downvotes;
    LocalDateTime createdAt;
}
```

### Comment
Represents a comment made by a user on a post (question or answer).

```java
class Comment {
    String commentId;
    String postId;
    String userId;
    String text;
    int upvotes;
    LocalDateTime createdAt;
}
```

### Badge (Optional)
Represents user achievement or reputation badges.

```java
class Badge {
    String badgeId;
    String name;
    String description;
}
```

## Service Layer Design

### UserService
Handles all CRUD operations related to users and profiles.

```java
class UserService {
    User createUser(User user);
    User getUser(String userId);
    void updateUser(User user);
    void deleteUser(String userId);
}
```

### PostService
Manages creation, retrieval, and voting operations on posts and answers.

```java
class PostService {
    Post createPost(Post post);
    List<Post> getPostsByUser(String userId);
    List<Post> getQuestions();
    List<Post> getAnswersForQuestion(String questionId);
    void upvotePost(String postId);
    void downvotePost(String postId);
}
```

### CommentService
Handles comment-related operations for posts.

```java
class CommentService {
    Comment addComment(Comment comment);
    List<Comment> getCommentsForPost(String postId);
}
```

### FeedRecommendationStrategy (Interface)
Defines a strategy interface for personalized feed generation.

```java
interface FeedRecommendationStrategy {
    List<Post> getRecommendedPosts(User user);
}

Implementations
RecommendationByPreference

RecommendationByProfileInterests

RecommendationByPastHistory

```

### FeedService
Uses Strategy Pattern to fetch personalized feeds for a user.

```java
class FeedService {
    private FeedRecommendationStrategy strategy;
    private PostService postService;

    public FeedService(FeedRecommendationStrategy strategy, PostService postService) {
        this.strategy = strategy;
        this.postService = postService;
    }

    List<Post> getFeed(User user) {
        return strategy.getRecommendedPosts(user);
    }
}
```

### VoteService (Optional)
Manages upvotes/downvotes on posts and comments.

```java
class VoteService {
    void upvotePost(String postId, String userId);
    void downvotePost(String postId, String userId);
    void upvoteComment(String commentId, String userId);
    void downvoteComment(String commentId, String userId);
}
```

## Relationships Summary

| Entity | Relationship | Type |
|--------|---------------|------|
| User – Profile | 1 : 1 |
| User – Post | 1 : N |
| Post (Question) – Post (Answer) | 1 : N |
| Post – Comment | 1 : N |
| User – Comment | 1 : N |

---

##  Design Patterns Used

| Pattern | Purpose |
|----------|----------|
| **Strategy Pattern** | For feed recommendation system |
| **Factory Pattern (optional)** | For creating post types (Question/Answer) |
| **Observer Pattern (optional)** | For notifications when an answer/comment is added |
| **DAO/Repository Layer** | For database abstraction |

---

## Future Extensions

- **TagService** – Manage tags and search by topic  
- **NotificationService** – Notify users for answers or comments  
- **SearchService** – Full-text search (Elasticsearch)  
- **Caching Layer** – Cache trending questions or feeds  
- **ReputationSystem** – Assign reputation points to active users  

---

## UML Overview (Conceptual)

```text
User ── 1 ─── 1 ── Profile
User ── 1 ─── N ── Post
Post ── 1 ─── N ── Comment
Post (Question) ── 1 ─── N ── Post (Answer)
```