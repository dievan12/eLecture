package lecture.dao;

import lecture.domain.User;
import lecture.util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    //Creates a user to add to the Database
    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO Users (Name, Role, Password) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getRole());
            stmt.setString(3, user.getPassword()); // This should be the hashed password
            stmt.executeUpdate();
        }
    }


    // Finds a User based of the ID
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM Users WHERE User_ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("User_ID"),
                            rs.getString("Name"),
                            rs.getString("Role"),
                            rs.getString("Password"));
                }
            }
        }
        return null;
    }


    // Updates User
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE Users SET Name = ?, Role = ?, Password = ? WHERE User_ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getRole());
            stmt.setString(3, user.getPassword()); // This should be the hashed password
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();
        }
    }


        // Deletes User
        public void deleteUser ( int id) throws SQLException {
            String sql = "DELETE FROM Users WHERE User_ID = ?";
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        }

        // List all Users from the database
        public List<User> getAllUsers () throws SQLException {
            List<User> users = new ArrayList<>();
            String sql = "SELECT * FROM Users";
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                            rs.getInt("User_ID"),
                            rs.getString("Name"),
                            rs.getString("Role"),
                            rs.getString("Password")));
                }
            }
            return users;
        }

    }

