import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Controller {
    @FXML private TextField mp1txt_TextField;
    @FXML private RadioButton CPUbf_RadioButton, GPUbf_RadioButton;

    @FXML private void Browse_Button() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select your movable_part1");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) mp1txt_TextField.setText(file.getAbsolutePath());
    }

    @FXML private void Bruteforce_Button() {
        if (!CPUbf_RadioButton.isSelected() && !GPUbf_RadioButton.isSelected()) {
            Main.showAlertBox("Invalid bruteforce mode! Choose either a CPU bruteforce or a GPU bruteforce");
            return;
        }

        String mp1Str = mp1txt_TextField.getText();
        Path mp1 = Paths.get(mp1txt_TextField.getText());

        if (mp1Str.isEmpty() || !Files.exists(mp1)){
            Main.showAlertBox("Could not find the file specified!");
            return;
        }

        try {
            Seedminer miner = new Seedminer(mp1, GPUbf_RadioButton.isSelected());
            boolean isNew3DS = miner.getNew3DS();
            DataNodes downloader = new DataNodes();
            byte[] nodes = downloader.GetDataNodes(isNew3DS);
            miner.parseNodes(nodes);
            miner.DoSeedminer();

        } catch (IOException | NumberFormatException e) {
            Main.showAlertBox(e.getMessage());
        }
    }
}
