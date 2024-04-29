package lecture.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lecture.util.DatabaseConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class Register {
    @FXML
    private TextField usernameText; // FX ID for username
    @FXML
    private TextField passwordText; // FX ID for password
    @FXML
    private TextField roleText;     // FX ID for role
    @FXML
    private Button loginReturn;     // FX ID for the return to login button
    @FXML
    private Label loginMessage;     // FX ID for the message label

    public void registerButtonAction(ActionEvent event) {
        String username = usernameText.getText();
        String password = passwordText.getText();
        String role = roleText.getText();

        if (username.isBlank() || password.isBlank() || role.isBlank()) {
            loginMessage.setText("Please enter a username, password, and role.");
        } else {
            if (registerNewUser(username, password, role)) {
                loginMessage.setText("Registration successful.");
                returnToLoginAction(event);
            } else {
                loginMessage.setText("Registration failed.");
            }
        }
    }

    private boolean registerNewUser(String username, String password, String role) {
        String insertQuery = "INSERT INTO Users (Name, Password, Role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            loginMessage.setText("SQL Error: " + e.getMessage());
            return false;
        }
    }

    public void returnToLoginAction(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login.fxml")));
            Scene loginScene = new Scene(loginRoot);
            Stage stage = (Stage) loginReturn.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            loginMessage.setText("Error loading the login screen.");
        }
    }
}
