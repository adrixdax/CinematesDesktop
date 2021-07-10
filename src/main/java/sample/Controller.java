package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Controller {

    private Stage stage;
    private Parent root;
    @FXML
    public Button LoginButton;

    public void usernameAndPasswordLogin (MouseEvent mouseEvent){
        if (mouseEvent.getClickCount() == 1) {
            System.out.println("Ho cliccato");
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../sample.fxml")));
                stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
                stage.setScene(new Scene(root, 1280, 720));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
