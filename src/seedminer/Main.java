package seedminer;

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
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("MainGUI.fxml")), 700, 323));
        primaryStage.setTitle("Seedminer");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void showAlertBox(String Title, String Header, String Content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Title);
        alert.setHeaderText(Header); //can be null
        alert.setContentText(Content);
        alert.showAndWait();
    }
}
