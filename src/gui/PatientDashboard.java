package gui;

import exception.AppointmentConflictException;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.stream.Collectors;

public class PatientDashboard {

    private Stage stage;
    private User user;
    private AppointmentService appointmentService;
    private RecordService      recordService;
    private UserService        userService;

    public PatientDashboard(Stage stage, User user) {
        this.stage              = stage;
        this.user               = user;
        this.appointmentService = new AppointmentService();
        this.recordService      = new RecordService();
        this.userService        = new UserService();
    }

    public void show() {
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: #f3e5f5;");
        tabPane.getTabs().addAll(
            dashboardTab(),
            doctorsTab(),
            appointmentsTab(),
            recordsTab()
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, 800, 550);
        stage.setTitle("RHMS — Patient: " + user.getName());
        stage.setScene(scene);
        stage.show();
    }

    // ── Tab 1: Dashboard ──
    private Tab dashboardTab() {
        Tab tab = new Tab("Dashboard");

        VBox box = new VBox(16);
        box.setPadding(new Insets(30));
        box.setStyle("-fx-background-color: #f3e5f5;");

        Text welcome = new Text("Welcome, " + user.getName());
        welcome.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        welcome.setFill(Color.web("#4a148c"));

        Patient p = (Patient) user;

        Label info1 = styledLabel("Blood Group : " + p.getBloodGroup());
        Label info2 = styledLabel("Age         : " + p.getAge());
        Label info3 = styledLabel("Email       : " + user.getEmail());
        Label info4 = styledLabel("Role        : " + user.getRole());

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(logoutStyle());
        logoutBtn.setOnAction(e -> new LoginScreen(stage).show());

        box.getChildren().addAll(welcome, info1, info2, info3, info4, logoutBtn);
        tab.setContent(box);
        tab.setStyle(tabStyle());
        return tab;
    }

    // ── Tab 2: Available Doctors ──
    private Tab doctorsTab() {
        Tab tab = new Tab("Available Doctors");

        VBox box = new VBox(14);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #f3e5f5;");

        Label heading = new Label("Available Doctors");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #4a148c;");

        TableView<User> table = new TableView<>();
        table.setStyle("-fx-border-color: #ce93d8;");

        TableColumn<User, String> colId   = col("Doctor ID", "userId", 100);
        TableColumn<User, String> colName = col("Name",      "name",   150);

        TableColumn<User, String> colSpec = new TableColumn<>("Specialization");
        colSpec.setPrefWidth(150);
        colSpec.setCellValueFactory(data -> {
            User u = data.getValue();
            if (u instanceof Doctor) {
                return new SimpleStringProperty(((Doctor) u).getSpecialization());
            }
            return new SimpleStringProperty("");
        });

        TableColumn<User, String> colDays = new TableColumn<>("Available Days");
        colDays.setPrefWidth(160);
        colDays.setCellValueFactory(data -> {
            User u = data.getValue();
            if (u instanceof Doctor) {
                return new SimpleStringProperty(((Doctor) u).getAvailableDays());
            }
            return new SimpleStringProperty("");
        });

        table.getColumns().addAll(colId, colName, colSpec, colDays);

        List<User> doctors = userService.getAllUsers()
            .stream()
            .filter(u -> u.getRole().equals("DOCTOR"))
            .collect(Collectors.toList());

        table.setItems(FXCollections.observableArrayList(doctors));

        Label hint = new Label("Use the Doctor ID above when booking an appointment.");
        hint.setStyle("-fx-font-size: 12px; -fx-text-fill: #7b1fa2;");

        box.getChildren().addAll(heading, table, hint);
        tab.setContent(box);
        tab.setStyle(tabStyle());
        return tab;
    }

    // ── Tab 3: Appointments ──
    private Tab appointmentsTab() {
        Tab tab = new Tab("Appointments");

        VBox box = new VBox(14);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #f3e5f5;");

        Label heading = new Label("My Appointments");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #4a148c;");

        TableView<Appointment> table = new TableView<>();
        table.setStyle("-fx-border-color: #ce93d8;");

        TableColumn<Appointment, String> colId     = col("Appointment ID", "appointmentId", 140);
        TableColumn<Appointment, String> colDoc    = col("Doctor ID",      "doctorId",      120);
        TableColumn<Appointment, String> colDate   = col("Date",           "date",          100);
        TableColumn<Appointment, String> colTime   = col("Time",           "time",           80);
        TableColumn<Appointment, String> colStatus = col("Status",         "status",        100);

        table.getColumns().addAll(colId, colDoc, colDate, colTime, colStatus);
        refreshAppointments(table);

        Label formHeading = new Label("Book New Appointment");
        formHeading.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #6a1b9a;");

        TextField doctorIdField = new TextField();
        doctorIdField.setPromptText("Doctor ID  e.g. D001");
        doctorIdField.setMaxWidth(220);
        doctorIdField.setStyle(fieldStyle());

        TextField dateField = new TextField();
        dateField.setPromptText("Date  e.g. 2025-06-10");
        dateField.setMaxWidth(220);
        dateField.setStyle(fieldStyle());

        TextField timeField = new TextField();
        timeField.setPromptText("Time  e.g. 10:00");
        timeField.setMaxWidth(220);
        timeField.setStyle(fieldStyle());

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #c62828; -fx-font-size: 12px;");

        Label successLabel = new Label("");
        successLabel.setStyle("-fx-text-fill: #2e7d32; -fx-font-size: 12px;");

        Button bookBtn = new Button("Book Appointment");
        bookBtn.setStyle(btnStyle());

        bookBtn.setOnAction(e -> {
            errorLabel.setText("");
            successLabel.setText("");

            String doctorId = doctorIdField.getText().trim();
            String date     = dateField.getText().trim();
            String time     = timeField.getText().trim();

            if (doctorId.isEmpty() || date.isEmpty() || time.isEmpty()) {
                errorLabel.setText("Please fill all fields.");
                return;
            }

            String aptId = "APT" + System.currentTimeMillis();
            Appointment apt = new Appointment(
                aptId, user.getUserId(), doctorId, date, time
            );

            try {
                appointmentService.book(apt);
                successLabel.setText("Appointment booked successfully.");
                doctorIdField.clear();
                dateField.clear();
                timeField.clear();
                refreshAppointments(table);
            } catch (AppointmentConflictException ex) {
                errorLabel.setText("Conflict: " + ex.getMessage());
            }
        });

        HBox form = new HBox(10, doctorIdField, dateField, timeField, bookBtn);

        box.getChildren().addAll(
            heading, table, formHeading, form, errorLabel, successLabel
        );
        tab.setContent(box);
        tab.setStyle(tabStyle());
        return tab;
    }

    // ── Tab 4: Medical Records ──
    private Tab recordsTab() {
        Tab tab = new Tab("Medical Records");

        VBox box = new VBox(14);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #f3e5f5;");

        Label heading = new Label("My Medical Records");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #4a148c;");

        TableView<MedicalRecord> table = new TableView<>();
        table.setStyle("-fx-border-color: #ce93d8;");

        TableColumn<MedicalRecord, String> colId   = col("Record ID",  "recordId",  120);
        TableColumn<MedicalRecord, String> colDoc  = col("Doctor ID",  "doctorId",  100);
        TableColumn<MedicalRecord, String> colDate = col("Date",       "date",      100);
        TableColumn<MedicalRecord, String> colDiag = col("Diagnosis",  "diagnosis", 150);
        TableColumn<MedicalRecord, String> colNote = col("Notes",      "notes",     200);

        table.getColumns().addAll(colId, colDoc, colDate, colDiag, colNote);

        List<MedicalRecord> records = recordService.getByPatientId(user.getUserId());
        table.setItems(FXCollections.observableArrayList(records));

        box.getChildren().addAll(heading, table);
        tab.setContent(box);
        tab.setStyle(tabStyle());
        return tab;
    }

    // ── Helpers ──
    private void refreshAppointments(TableView<Appointment> table) {
        List<Appointment> list = appointmentService.getByPatientId(user.getUserId());
        table.setItems(FXCollections.observableArrayList(list));
    }

    private <T> TableColumn<T, String> col(String title, String field, int width) {
        TableColumn<T, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(field));
        col.setPrefWidth(width);
        return col;
    }

    private Label styledLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 13px; -fx-text-fill: #4a148c;");
        return l;
    }

    private String fieldStyle() {
        return "-fx-border-color: #ce93d8; -fx-border-radius: 6px;" +
               "-fx-background-radius: 6px; -fx-padding: 6px; -fx-font-size: 12px;";
    }

    private String btnStyle() {
        return "-fx-background-color: #4a148c; -fx-text-fill: white;" +
               "-fx-font-size: 12px; -fx-border-radius: 6px;" +
               "-fx-background-radius: 6px; -fx-padding: 8px; -fx-cursor: hand;";
    }

    private String logoutStyle() {
        return "-fx-background-color: #7b1fa2; -fx-text-fill: white;" +
               "-fx-font-size: 12px; -fx-border-radius: 6px;" +
               "-fx-background-radius: 6px; -fx-padding: 8px; -fx-cursor: hand;";
    }

    private String tabStyle() {
        return "-fx-background-color: #f3e5f5;";
    }
}