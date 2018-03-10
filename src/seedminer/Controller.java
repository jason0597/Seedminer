package seedminer;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Controller {
    @FXML private TextField mp1txt_TextField;
    @FXML private RadioButton CPUbf_RadioButton, GPUbf_RadioButton;

    @FXML private void Browse_Button() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select your movable_part1.txt file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt", "*.txt"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) mp1txt_TextField.setText(file.getAbsolutePath());
    }

    @FXML private void Bruteforce_Button() {
        Path mp1_txt = Paths.get("C:\\Users\\jason\\Desktop\\movable_part1.txt");

        if (!CPUbf_RadioButton.isSelected() && !GPUbf_RadioButton.isSelected()) {
            Main.showAlertBox("An exception occurred!", null, "Invalid bruteforce mode! Choose either a CPU bruteforce or a GPU bruteforce");
            return;
        }

        Seedminer miner = new Seedminer(mp1_txt, CPUbf_RadioButton.isSelected() ? 'c' : 'g');

        try {
            miner.DoSeedminer();
        } catch (IOException e) {
            Main.showAlertBox("An exception occurred!", null, e.getMessage());
        } catch (NumberFormatException e) {
            Main.showAlertBox("An exception occurred!", null, "Failed to parse the LFCS/ID0 bytes!");
        }
    }
}
