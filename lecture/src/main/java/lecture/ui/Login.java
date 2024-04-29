package lecture.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lecture.domain.User;
import lecture.util.DatabaseConnector;
import lecture.util.SessionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    @FXML
    private Button Registerbutton;
    @FXML
    private Label loginMessage;
    @FXML
    private TextField usernameText;
    @FXML
    private PasswordField passwordText;

    @FXML
    public void loginButtonAction(ActionEvent event) {
        String username = usernameText.getText();
        String password = passwordText.getText();

        if (username.isBlank() || password.isBlank()) {
            loginMessage.setText("Please enter username and password.");
            return;
        }

        User user = validateLogin(username, password);
        if (user != null) {
            SessionManager.getInstance().setCurrentUser(user);
            switchToDashboard(user.getRole()); // Pass the user's role
        } else {
            loginMessage.setText("Invalid username or password. Please try again.");
        }
    }

    private void switchToDashboard(String role) {

        Stage stage = (Stage) usernameText.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        String fxmlPath;

        if ("Lecturer".equalsIgnoreCase(role)) {
            fxmlPath = "/lecturerdashboard.fxml";
            stage.setTitle("Lecturer Dashboard - eLecture");
        } else {
            fxmlPath = "/dashboard.fxml";
            stage.setTitle("Dashboard - eLecture");
        }

        try {
            loader.setLocation(getClass().getResource(fxmlPath));
            root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            if ("Lecturer".equalsIgnoreCase(role)) {
                LecturerDashboard controller = loader.getController();
                controller.setUsername(SessionManager.getInstance().getCurrentUser().getName());
            } else {
                Dashboard controller = loader.getController();
                controller.setUsername(SessionManager.getInstance().getCurrentUser().getName());
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            loginMessage.setText("Error when trying to switch to the dashboard.");
        }
    }


    private User validateLogin(String username, String password) {
        String query = "SELECT * FROM Users WHERE Name = ? AND Password = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // In production, hash the password before comparing
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("User_ID");
                String name = rs.getString("Name");
                String role = rs.getString("Role");
                return new User(id, name, role, password); // Correct order: ID, Name, Role, Password
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void registerButtonAction(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent registerRoot = fxmlLoader.load();

            Stage stage = (Stage) Registerbutton.getScene().getWindow();

            Scene registerScene = new Scene(registerRoot);

            stage.setScene(registerScene);
            stage.setTitle("Register");
            stage.show();
        } catch (IOException ex) {
            System.err.println("Failed to create new Window." + ex);
        }
    }

}
