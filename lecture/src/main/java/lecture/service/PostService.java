package lecture.service;

import lecture.dao.PostDAO;
import lecture.domain.Post;
import lecture.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostService {
    private static final PostDAO postDAO = new PostDAO();

    public Post getPostById(int postId) {
        try {
            return postDAO.getPostById(postId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get post by ID", e);
        }
    }

    public void createPost(Post post) {
        try {
            postDAO.createPost(post);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create post", e);
        }
    }

    public void updatePost(Post post) throws SQLException {
        String sql = "UPDATE Posts SET Likes = Likes + 1 WHERE Post_ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, post.getId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating post failed, no rows affected.");
            }
        }
    }


    public static void deletePost(int postId) {
        try {
            postDAO.deletePost(postId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete post", e);
        }
    }

    public void likePost(int postId) {
        try {
            Post post = getPostById(postId);
            if (post == null) {
                throw new RuntimeException("Post not found with ID: " + postId);
            }
            post.addLike(); // This assumes you have a method in Post that increments the like count
            updatePost(post); // Persist the changes to the database
        } catch (Exception e) {
            // Log error
            throw new RuntimeException("Failed to like post", e);
        }
    }
}
