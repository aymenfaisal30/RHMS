package gui;

import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import model.*;
import service.*;

import java.util.List;

public class AdminPanel {

    private Stage stage;
    private User  user;
    private UserService        userService;
    private AppointmentService appointmentService;

    public AdminPanel(Stage stage, User user) {
        this.stage              = stage;
        this.user               = user;
        this.userService        = new UserService();
        this.appointmentService = new AppointmentService();
    }

    public void show() {
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: #f3e5f5;");
        tabPane.getTabs().addAll(
            usersTab(),
            appointmentsTab()
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, 800, 550);
        stage.setTitle("RHMS — Admin: " + user.getName());
        stage.setScene(scene);
        stage.show();
    }

    private Tab usersTab() {
        Tab tab = new Tab("All Users");

        VBox box = new VBox(14);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #f3e5f5;");

        Text heading = new Text("All Registered Users");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        heading.setFill(Color.web("#4a148c"));

        TableView<User> table = new TableView<>();
        table.setStyle("-fx-border-color: #ce93d8;");

        TableColumn<User, String> colId    = col("User ID", "userId",   100);
        TableColumn<User, String> colName  = col("Name",    "name",     150);
        TableColumn<User, String> colEmail = col("Email",   "email",    200);
        TableColumn<User, String> colRole  = col("Role",    "role",      100);

        table.getColumns().addAll(colId, colName, colEmail, colRole);
        refreshUsers(table);

        // ── Remove user ──
        TextField removeField = new TextField();
        removeField.setPromptText("Enter User ID to remove");
        removeField.setMaxWidth(250);
        removeField.setStyle(fieldStyle());

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #c62828; -fx-font-size: 12px;");

        Label successLabel = new Label("");
        successLabel.setStyle("-fx-text-fill: #2e7d32; -fx-font-size: 12px;");

        Button removeBtn = new Button("Remove User");
        removeBtn.setStyle(
            "-fx-background-color: #c62828; -fx-text-fill: white;" +
            "-fx-border-radius: 6px; -fx-background-radius: 6px;" +
            "-fx-padding: 8px; -fx-cursor: hand;"
        );

        removeBtn.setOnAction(e -> {
            String id = removeField.getText().trim();
            if (id.isEmpty()) {
                errorLabel.setText("Please enter a User ID.");
                return;
            }
            boolean removed = userService.removeById(id);
            if (removed) {
                successLabel.setText("User removed successfully.");
                errorLabel.setText("");
                refreshUsers(table);
                removeField.clear();
            } else {
                errorLabel.setText("User ID not found.");
                successLabel.setText("");
            }
        });

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(logoutStyle());
        logoutBtn.setOnAction(e -> new LoginScreen(stage).show());

        HBox removeRow = new HBox(10, removeField, removeBtn);
        box.getChildren().addAll(heading, table, removeRow, errorLabel, successLabel, logoutBtn);
        tab.setContent(box);
        return tab;
    }

    private Tab appointmentsTab() {
        Tab tab = new Tab("All Appointments");

        VBox box = new VBox(14);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #f3e5f5;");

        Text heading = new Text("All Appointments");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        heading.setFill(Color.web("#4a148c"));

        TableView<Appointment> table = new TableView<>();
        table.setStyle("-fx-border-color: #ce93d8;");

        TableColumn<Appointment, String> colId     = col("Appointment ID", "appointmentId", 140);
        TableColumn<Appointment, String> colPat    = col("Patient ID",     "patientId",     120);
        TableColumn<Appointment, String> colDoc    = col("Doctor ID",      "doctorId",      120);
        TableColumn<Appointment, String> colDate   = col("Date",           "date",          100);
        TableColumn<Appointment, String> colTime   = col("Time",           "time",           80);
        TableColumn<Appointment, String> colStatus = col("Status",         "status",        100);

        table.getColumns().addAll(colId, colPat, colDoc, colDate, colTime, colStatus);

        List<Appointment> list = appointmentService.getAllAppointments();
        table.setItems(FXCollections.observableArrayList(list));

        box.getChildren().addAll(heading, table);
        tab.setContent(box);
        return tab;
    }

    private void refreshUsers(TableView<User> table) {
        table.setItems(FXCollections.observableArrayList(userService.getAllUsers()));
    }

    private <T> TableColumn<T, String> col(String title, String field, int width) {
        TableColumn<T, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(field));
        col.setPrefWidth(width);
        return col;
    }

    private String fieldStyle() {
        return "-fx-border-color: #ce93d8; -fx-border-radius: 6px;" +
               "-fx-background-radius: 6px; -fx-padding: 6px; -fx-font-size: 12px;";
    }

    private String logoutStyle() {
        return "-fx-background-color: #7b1fa2; -fx-text-fill: white;" +
               "-fx-font-size: 12px; -fx-border-radius: 6px;" +
               "-fx-background-radius: 6px; -fx-padding: 8px; -fx-cursor: hand;";
    }
}