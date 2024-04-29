package lecture.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Post {
    private static final AtomicInteger generateID = new AtomicInteger();
    private int id; // Made non-final to allow setting from database
    private final String content;
    private final User user;
    private int likes;
    private boolean inappropriate;
    private final List<Comment> comments; // To store comments associated with this post

    public Post(String content, User user, boolean inappropriate) {
        this.id = generateID.incrementAndGet();
        this.content = content;
        this.user = user;
        this.inappropriate = inappropriate;
        this.comments = new ArrayList<>();
        this.likes = 0;
    }

    public Post(int id, String content, User user, int likes, boolean inappropriate) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.likes = likes;
        this.inappropriate = inappropriate;
        this.comments = new ArrayList<>();
    }

    @Override
    public String toString() {
        // You can customize this string to display whatever information you want about the post
        return user.getName() + ": " + content + " (Likes: " + likes + ")";
    }


    public void incrementLikes() {
        this.likes++;
    }



    // Method to add a comment to this post
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public Comment getComment(int commentId) {
        return this.comments.stream()
                .filter(comment -> comment.getCommentID().equals(commentId))
                .findFirst()
                .orElse(null); // Return null if no comment matches the provided ID
    }

    public void deleteComment(Integer commentId) {
        comments.removeIf(comment -> comment.getCommentID().equals(commentId));
    }

    public int getId() {
        return id;
    }

    public void setPostID(int id) {
        this.id = id; // Setter to allow setting ID from DAO
    }

    public void addLike() {
        this.likes++;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public void markAsInappropriate() {
        this.inappropriate = true;
    }

    public boolean isInappropriate() {
        return inappropriate;
    }

    public List<Comment> getComments() {
        return new ArrayList<>(this.comments);
    }

    public int getLikes() {
        return this.likes;
    }
}
