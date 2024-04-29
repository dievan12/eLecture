package lecture.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import lecture.domain.*;
import lecture.service.*;
import lecture.dao.*;
import lecture.util.DatabaseConnector;
import lecture.util.SessionManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Dashboard extends Application implements NotiListener {

    public Label usernameLabel;
    public Button uploadPost;
    public Button refreshFeed;
    public Label currentUsername;
    @FXML
    private ListView<Post> posts;
    public TextArea postTextArea;
    @FXML
    private TextArea notiArea;
    private NotiService notiService;
    private final PostDAO postDAO = new PostDAO(); // Create an instance of PostDAO

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            Parent root = loader.load();


            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("eLecture");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading the dashboard: " + e.getMessage());
        }
    }

    public void setUsername(String username) {
        if (usernameLabel != null) {
            usernameLabel.setText("Hi, " + username);
        }
    }

    @Override
    public void onNotiReceived(Notification noti) {
        Platform.runLater(() -> {
            notiArea.appendText(formatNotiDisplay(noti) + "\n");
        });
    }

    private String formatNotiDisplay(Notification notification) {
        return String.format("%s - %s: %s",
                notification.getTimestamp(),
                notification.getRecipient().getName(),
                notification.getMessage());
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        SessionManager.getInstance().setCurrentUser(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(loginRoot);
            stage.setScene(scene);
            stage.setTitle("eLecture");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Action for when "Upload Post" is selected

    @FXML
    public void onUploadAction(ActionEvent event) {
        String content = postTextArea.getText();
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (content.isEmpty()) {
            showAlert("Post content cannot be empty.", Alert.AlertType.WARNING);
            return;
        }

        if (currentUser == null) {
            showAlert("No user is currently logged in.", Alert.AlertType.ERROR);
            return;
        }

        Post newPost = new Post(content, currentUser, false);

        try {
            postDAO.createPost(newPost);
            posts.getItems().add(0, newPost);
            postTextArea.clear();
            showAlert("Post uploaded successfully!", Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            showAlert("Failed to upload the post. Error: " + ex.getMessage(), Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }

    private void showAlert(String content, Alert.AlertType type) {
        // Utility method to show alerts to the user
        Alert alert = new Alert(type, content);
        alert.showAndWait();
    }

    @FXML
    public void onRefreshAction(ActionEvent event) {
        refreshFeed();
    }

    private void refreshFeed() {
        try {
            List<Post> updatedPosts = postDAO.getAllPosts(); // Assuming this method fetches all posts
            posts.getItems().clear(); // Clear the current items
            posts.getItems().addAll(updatedPosts); // Add all the fetched posts
        } catch (SQLException e) {
            showAlert("Error refreshing feed: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private User getCurrentUser() {
        int userId = getCurrentUserID();

        String sql = "SELECT * FROM Users WHERE User_ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("User_ID");
                    String name = rs.getString("Name");
                    String password = rs.getString("Password");
                    String role = rs.getString("Role");
                    return new User(id, name, password, role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getCurrentUserID() {
        return 0;
    }

    public void initialize() {
        posts.setCellFactory(param -> new ListCell<Post>() {
            private final Button likeButton = new Button("Like");
            private final HBox hbox = new HBox(5);
            private final Label label = new Label();
            private final Label likesLabel = new Label();
            private final Pane pane = new Pane();



            {
                HBox.setMargin(likesLabel, new Insets(0, 10, 0, 0));

                hbox.getChildren().addAll(label, pane, likesLabel, likeButton);
                HBox.setHgrow(pane, Priority.ALWAYS);
                likeButton.setOnAction(event -> handleLikeButtonAction());
            }

            private void handleLikeButtonAction() {
                Post post = getItem();
                if (post != null) {
                    post.addLike();
                    likesLabel.setText("Likes: " + post.getLikes());

                }
            }

            @Override
            protected void updateItem(Post post, boolean empty) {
                super.updateItem(post, empty);
                if (empty || post == null) {
                    setGraphic(null);
                } else {

                    label.setText(post.getUser().getName() + ": " + post.getContent());
                    likesLabel.setText("Likes: " + post.getLikes());
                    setGraphic(hbox);
                }
            }
        });



    }
}