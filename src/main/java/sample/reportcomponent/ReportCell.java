package sample.reportcomponent;

import component.db.Report;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

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
    private Button Remove;

    @FXML
    private Button Watch;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(ReportedReviews reportedReviews, boolean empty) {


        super.updateItem(reportedReviews, empty);

        if(empty || reportedReviews == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/ListCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            User.setText(String.valueOf("adriano");
            ReportType.setText(reportedReviews.getReportType());
            Reviews.setText(reportedReviews.getTitle() + "\n" + reportedReviews.getDescription());


            setText(null);
            setGraphic(gridpane);
        }

    }
}
