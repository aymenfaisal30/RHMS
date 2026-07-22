package gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        LoginScreen loginScreen = new LoginScreen(stage);
        loginScreen.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}