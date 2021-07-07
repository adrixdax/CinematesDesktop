package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Controller {

    @FXML
    private Button LoginButton;

    public void usernameAndPasswordLogin(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount()==1){
            System.out.println("Ho cliccato");
            //FirebaseLogin
        }
    }
}
