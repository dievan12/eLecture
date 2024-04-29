package lecture.domain;

import lecture.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Page {
    private final List<Post> posts;
    private final List<Comment> comments;
    private final NotiService notificationService;

    public Page(NotiService notificationService) {
        this.posts = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.notificationService = notificationService;
    }

    public void addPost(Post post) {
        Objects.requireNonNull(post, "Post cannot be null");
        this.posts.add(post);
        notificationService.notifyUser(post.getUser(), "Your post has been added.");
    }

    public boolean deletePost(User user, int postId) {
        return posts.removeIf(post ->
                post.getId() == postId &&
                        (user instanceof Lecturer || post.getUser().equals(user))
        );
    }

    public Comment getComment(Integer commentID) {
        return comments.stream()
                .filter(c -> c.getCommentID().equals(commentID))
                .findFirst()
                .orElse(null);
    }

    public List<Post> getPosts() {
        return new ArrayList<>(posts);
    }

    public List<Comment> getComments() {
        return new ArrayList<>(comments);
    }

    public void addComment(Comment comment) {
        Objects.requireNonNull(comment, "Comment cannot be null");
        this.comments.add(comment);
        notificationService.notifyUser(comment.getUser(), "Your comment has been added.");
    }

    public boolean deleteComment(User user, int commentId) {
        return comments.removeIf(comment ->
                comment.getCommentID() == commentId &&
                        (user instanceof Lecturer || comment.getUser().equals(user))
        );
    }

    public void markPostAsInappropriate(int postId, User user) {
        if (user instanceof Lecturer) {
            posts.stream()
                    .filter(p -> p.getId() == postId)
                    .findFirst()
                    .ifPresent(p -> {
                        p.markAsInappropriate();
                        notificationService.notifyUser(p.getUser(), "Your post has been marked as inappropriate.");
                    });
        }
    }
}


