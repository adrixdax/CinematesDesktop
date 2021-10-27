package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import sample.retrofit.RetrofitResponse;

import java.util.Optional;


public class AddAdmin  {

    @FXML
    private javafx.scene.control.TextField textField;

    public void queryByEmail(){
        RetrofitResponse.getResponse(textField.getText(),this,"makeAdmin");

    }

    public void setResponse(String response) {
        Platform.runLater(() -> {
            Alert alert= null;
            if(response.equals("Utente non trovato")){
                 alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("Attenzione!");
            }else{
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(textField.getText() + " è stato aggiunto come admin!");
            }
            alert.setHeaderText(response);
            Optional<ButtonType> result = alert.showAndWait();
            alert.close();
        });

    }

}
