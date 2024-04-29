package lecture.service;

import lecture.dao.CommentDAO;
import lecture.domain.Comment;

public class CommentService {
    private final CommentDAO commentDAO = new CommentDAO();

    public Comment getCommentById(int commentId) {
        try {
            return commentDAO.getCommentById(commentId);
        } catch (Exception e) {
            // Log error
            throw new RuntimeException("Failed to get comment by ID", e);
        }
    }

    public void createComment(Comment comment) {
        try {
            commentDAO.createComment(comment);
        } catch (Exception e) {
            // Log error
            throw new RuntimeException("Failed to create comment", e);
        }
    }

    public void deleteComment(int commentId) {
        try {
            commentDAO.deleteComment(commentId);
        } catch (Exception e) {
            // Log error
            throw new RuntimeException("Failed to delete comment", e);
        }
    }
}
