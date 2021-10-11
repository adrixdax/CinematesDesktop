package sample.components.crashcomponent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import component.crashlitycs.CrashReport;

import java.io.IOException;

public class CrashCell extends ListCell<CrashReport> {


    @FXML
    private Button details;

    @FXML
    private Label exceptionType;

    @FXML
    private Label issue;

    @FXML
    private GridPane gridpane;

    @FXML
    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(CrashReport crashReports, boolean empty) {
        super.updateItem(crashReports, empty);
        if(empty || crashReports == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/CrashCell.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            exceptionType.setText(crashReports.getExceptionType());
            issue.setText(crashReports.getIssueTitle() + "\n");
            setText(null);
            setGraphic(gridpane);
        }
    }

    public void showDetails(MouseEvent mouseEvent) {

    }



}
