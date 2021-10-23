package sample;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import sample.retrofit.RetrofitResponse;


public class AddAdmin {

    @FXML
    private javafx.scene.control.TextField textField;

    public void queryByEmail(){
        RetrofitResponse.getResponse(textField.getText(),this,"makeAdmin");
        ((Stage) textField.getScene().getWindow()).close();
    }

}
