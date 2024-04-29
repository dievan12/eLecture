package lecture.service;

import lecture.dao.UserDAO;
import lecture.domain.User;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public User getUserById(int userId) {
        try {
            return userDAO.getUserById(userId);
        } catch (Exception e) {
            // Log error
            throw new RuntimeException("Failed to get user by ID", e);
        }
    }

    public void createUser(User user) {
        try {
            userDAO.createUser(user);
        } catch (Exception e) {
            // Log error
            throw new RuntimeException("Failed to create user", e);
        }
    }

    public void updateUser(User user) {
        try {
            userDAO.updateUser(user);
        } catch (Exception e) {
            // Log error
            throw new RuntimeException("Failed to update user", e);
        }
    }

    public void deleteUser(int userId) {
        try {
            userDAO.deleteUser(userId);
        } catch (Exception e) {
            // Log error
            throw new RuntimeException("Failed to delete user", e);
        }
    }
}
