package lecture.dao;

import lecture.domain.Comment;
import lecture.domain.User;
import lecture.util.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {
    // Assuming that UserDAO is available to fetch User information
    private final UserDAO userDAO = new UserDAO();

    public void createComment(Comment comment) throws SQLException {
        String sql = "INSERT INTO Comments (Content, User_ID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, comment.getContent());
            stmt.setInt(2, comment.getUser().getId()); // Assuming User class has getId() method
            stmt.executeUpdate();
        }
    }

    public Comment getCommentById(int commentId) throws SQLException {
        String sql = "SELECT * FROM Comments WHERE Comment_ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = userDAO.getUserById(rs.getInt("User_ID"));
                    return new Comment(
                            rs.getInt("Comment_ID"),
                            rs.getString("Content"),
                            user,
                            0); // Likes are not stored in the database schema provided
                }
            }
        }
        return null;
    }

    public void updateComment(Comment comment) throws SQLException {
        String sql = "UPDATE Comments SET Content = ?, User_ID = ? WHERE Comment_ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, comment.getContent());
            stmt.setInt(2, comment.getUser().getId());
            stmt.setInt(3, comment.getCommentID());
            stmt.executeUpdate();
        }
    }

    public void deleteComment(int commentId) throws SQLException {
        String sql = "DELETE FROM Comments WHERE Comment_ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commentId);
            stmt.executeUpdate();
        }
    }

    public List<Comment> getAllComments() throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM Comments";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = userDAO.getUserById(rs.getInt("User_ID"));
                comments.add(new Comment(
                        rs.getInt("Comment_ID"),
                        rs.getString("Content"),
                        user,
                        0)); // Likes are not stored in the database schema provided
            }
        }
        return comments;
    }
}
