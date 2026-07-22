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

public class DoctorDashboard {

    private Stage stage;
    private User  user;
    private AppointmentService appointmentService;
    private RecordService      recordService;
    private UserService        userService;

    public DoctorDashboard(Stage stage, User user) {
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
            appointmentsTab(),
            addRecordTab()
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, 800, 550);
        stage.setTitle("RHMS — Doctor: " + user.getName());
        stage.setScene(scene);
        stage.show();
    }

    private Tab dashboardTab() {
        Tab tab = new Tab("Dashboard");

        VBox box = new VBox(16);
        box.setPadding(new Insets(30));
        box.setStyle("-fx-background-color: #f3e5f5;");

        Text welcome = new Text("Welcome, Dr. " + user.getName());
        welcome.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        welcome.setFill(Color.web("#4a148c"));

        Doctor d = (Doctor) user;
        Label info1 = styledLabel("Specialization : " + d.getSpecialization());
        Label info2 = styledLabel("Available Days : " + d.getAvailableDays());
        Label info3 = styledLabel("Email          : " + user.getEmail());

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(logoutStyle());
        logoutBtn.setOnAction(e -> new LoginScreen(stage).show());

        box.getChildren().addAll(welcome, info1, info2, info3, logoutBtn);
        tab.setContent(box);
        return tab;
    }

    private Tab appointmentsTab() {
        Tab tab = new Tab("My Appointments");

        VBox box = new VBox(14);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #f3e5f5;");

        Label heading = new Label("Appointments Assigned to Me");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #4a148c;");

        TableView<Appointment> table = new TableView<>();

        TableColumn<Appointment, String> colId     = col("Appointment ID", "appointmentId", 140);
        TableColumn<Appointment, String> colPat    = col("Patient ID",     "patientId",     120);
        TableColumn<Appointment, String> colDate   = col("Date",           "date",          100);
        TableColumn<Appointment, String> colTime   = col("Time",           "time",           80);
        TableColumn<Appointment, String> colStatus = col("Status",         "status",        100);

        table.getColumns().addAll(colId, colPat, colDate, colTime, colStatus);

        List<Appointment> list = appointmentService.getByDoctorId(user.getUserId());
        table.setItems(FXCollections.observableArrayList(list));

        box.getChildren().addAll(heading, table);
        tab.setContent(box);
        return tab;
    }

    private Tab addRecordTab() {
        Tab tab = new Tab("Add Medical Record");

        VBox box = new VBox(14);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #f3e5f5;");

        Label heading = new Label("Write Medical Record for Patient");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #4a148c;");

        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Patient ID  e.g. P001");
        patientIdField.setMaxWidth(300);
        patientIdField.setStyle(fieldStyle());

        TextField dateField = new TextField();
        dateField.setPromptText("Date  e.g. 2025-06-10");
        dateField.setMaxWidth(300);
        dateField.setStyle(fieldStyle());

        TextField diagnosisField = new TextField();
        diagnosisField.setPromptText("Diagnosis");
        diagnosisField.setMaxWidth(300);
        diagnosisField.setStyle(fieldStyle());

        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Notes...");
        notesArea.setMaxWidth(300);
        notesArea.setPrefRowCount(4);
        notesArea.setStyle(fieldStyle());

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #c62828; -fx-font-size: 12px;");

        Label successLabel = new Label("");
        successLabel.setStyle("-fx-text-fill: #2e7d32; -fx-font-size: 12px;");

        Button saveBtn = new Button("Save Record");
        saveBtn.setStyle(btnStyle());

        saveBtn.setOnAction(e -> {
            errorLabel.setText("");
            successLabel.setText("");

            String patientId = patientIdField.getText().trim();
            String date      = dateField.getText().trim();
            String diagnosis = diagnosisField.getText().trim();
            String notes     = notesArea.getText().trim();

            if (patientId.isEmpty() || date.isEmpty() || diagnosis.isEmpty()) {
                errorLabel.setText("Please fill Patient ID, Date and Diagnosis.");
                return;
            }

            String recId = "REC" + System.currentTimeMillis();
            MedicalRecord record = new MedicalRecord(
                recId, patientId, user.getUserId(), date, diagnosis, notes
            );
            recordService.addRecord(record);
            successLabel.setText("Record saved successfully.");
            patientIdField.clear();
            dateField.clear();
            diagnosisField.clear();
            notesArea.clear();
        });

        box.getChildren().addAll(heading, patientIdField, dateField,
                                  diagnosisField, notesArea, saveBtn,
                                  errorLabel, successLabel);
        tab.setContent(box);
        return tab;
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
}