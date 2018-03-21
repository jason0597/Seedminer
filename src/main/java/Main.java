package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) throws Exception{
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/main/resources/MainGUI.fxml")), 350, 255));
        primaryStage.setTitle("Seedminer");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void showAlertBox(String Content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An exception occurred!");
        alert.setHeaderText(null);
        alert.setContentText(Content);
        alert.showAndWait();
    }
}
