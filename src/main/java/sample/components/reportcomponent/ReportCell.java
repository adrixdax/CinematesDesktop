package sample.components.reportcomponent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.apache.http.client.fluent.Request;
import sample.retrofit.RetrofitResponse;

import javax.swing.text.Element;
import java.io.IOException;

public class ReportCell extends ListCell<ReportedReviews> {


    @FXML
    private Label User;

    @FXML
    private Label ReportType;

    @FXML
    private Label Reviews;

    @FXML
    private GridPane gridpane;

    @FXML
    private Button remove;

    @FXML
    private Button visible;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(ReportedReviews reportedReviews, boolean empty) {
        super.updateItem(reportedReviews, empty);
        if(empty || reportedReviews == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/ReportCell.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                User.setText(Request.Get("https://ingsw2021-default-rtdb.firebaseio.com/Users/"+this.getItem().getId_user()+"/nickname.json").execute().returnContent().toString().replaceAll("\"",""));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ReportType.setText(reportedReviews.getReportType());
            Reviews.setText(reportedReviews.getTitle() + "\n" + reportedReviews.getDescription());
            setText(null);
            setGraphic(gridpane);
        }
    }

    public void keepVisible(MouseEvent mouseEvent) {
        RetrofitResponse.getResponse("Type=PostRequest&visible=true&idReport="+ (this.getItem()).getIdReport(),this,"updateReport");
        this.updateItem(this.getItem(),true);
    }

    public void removeReview(MouseEvent mouseEvent) {
        RetrofitResponse.getResponse("Type=PostRequest&delete=true&idReviews="+ (this.getItem()).getId_recordRef(),this,"deleteReview");
        this.updateItem(this.getItem(),true);
    }


}
