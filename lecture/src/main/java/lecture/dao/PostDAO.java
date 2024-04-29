package lecture.dao;

import lecture.domain.Post;
import lecture.domain.User;
import lecture.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
    private final UserDAO userDAO = new UserDAO(); // Assuming UserDAO is correctly implemented

    public void createPost(Post post) throws SQLException {
        String sql = "INSERT INTO Posts (Content, User_ID, Likes, Inappropriate) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, post.getContent());
            stmt.setInt(2, post.getUser().getId());
            stmt.setInt(3, post.getLikes());
            stmt.setBoolean(4, post.isInappropriate());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setPostID(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Post getPostById(int postId) throws SQLException {
        String sql = "SELECT * FROM Posts WHERE Post_ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = userDAO.getUserById(rs.getInt("User_ID"));
                    return new Post(
                            rs.getInt("Post_ID"),
                            rs.getString("Content"),
                            user,
                            rs.getInt("Likes"),
                            rs.getBoolean("Inappropriate"));
                }
            }
        }
        return null;
    }

    public void updatePost(Post post) throws SQLException {
        String sql = "UPDATE Posts SET Content = ?, User_ID = ?, Likes = ?, Inappropriate = ? WHERE Post_ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, post.getContent());
            stmt.setInt(2, post.getUser().getId());
            stmt.setInt(3, post.getLikes());
            stmt.setBoolean(4, post.isInappropriate());
            stmt.setInt(5, post.getId());
            stmt.executeUpdate();
        }
    }

    public void deletePost(int postId) throws SQLException {
        String sql = "DELETE FROM Posts WHERE Post_ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.executeUpdate();
        }
    }

    public void incrementPostLikes(int postId) throws SQLException {
        String sql = "UPDATE Posts SET Likes = Likes + 1 WHERE Post_ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.executeUpdate();
        }
    }





    public List<Post> getAllPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM Posts";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = userDAO.getUserById(rs.getInt("User_ID"));
                posts.add(new Post(
                        rs.getInt("Post_ID"),
                        rs.getString("Content"),
                        user,
                        rs.getInt("Likes"),
                        rs.getBoolean("Inappropriate")));
            }
        }
        return posts;
    }
}
