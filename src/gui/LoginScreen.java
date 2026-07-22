package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import model.User;
import service.UserService;
import exception.UserNotFoundException;

public class LoginScreen {

    private Stage stage;
    private UserService userService;

    public LoginScreen(Stage stage) {
        this.stage       = stage;
        this.userService = new UserService();
    }

    public void show() {

        // ── Title ──
        Text title = new Text("RHMS");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setFill(Color.web("#4a148c"));

        Text subtitle = new Text("Remote Hospital Management System");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setFill(Color.web("#7b1fa2"));

        // ── Fields ──
        TextField emailField = new TextField();
        emailField.setPromptText("Email address");
        emailField.setMaxWidth(300);
        emailField.setStyle(fieldStyle());

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(300);
        passField.setStyle(fieldStyle());

        // ── Error label ──
        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #c62828; -fx-font-size: 12px;");

        // ── Login button ──
        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(300);
        loginBtn.setStyle(buttonStyle());

        loginBtn.setOnAction(e -> {
            String email    = emailField.getText().trim();
            String password = passField.getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter email and password.");
                return;
            }

            try {
                User user = userService.login(email, password);
                openDashboard(user);
            } catch (UserNotFoundException ex) {
                errorLabel.setText("Invalid email or password.");
            }
        });

        // ── Layout ──
        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setMaxWidth(380);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #ce93d8;" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 12px;" +
            "-fx-background-radius: 12px;"
        );
        card.getChildren().addAll(title, subtitle, emailField, passField, errorLabel, loginBtn);

        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: #f3e5f5;");

        Scene scene = new Scene(root, 600, 450);
        stage.setTitle("RHMS — Login");
        stage.setScene(scene);
        stage.show();
    }

    private void openDashboard(User user) {
        switch (user.getRole()) {
            case "PATIENT" -> new PatientDashboard(stage, user).show();
            case "DOCTOR"  -> new DoctorDashboard(stage, user).show();
            case "ADMIN"   -> new AdminPanel(stage, user).show();
        }
    }

    private String fieldStyle() {
        return "-fx-border-color: #ce93d8;" +
               "-fx-border-radius: 6px;" +
               "-fx-background-radius: 6px;" +
               "-fx-padding: 8px;" +
               "-fx-font-size: 13px;";
    }

    private String buttonStyle() {
        return "-fx-background-color: #4a148c;" +
               "-fx-text-fill: white;" +
               "-fx-font-size: 14px;" +
               "-fx-font-weight: bold;" +
               "-fx-border-radius: 6px;" +
               "-fx-background-radius: 6px;" +
               "-fx-padding: 10px;" +
               "-fx-cursor: hand;";
    }
}