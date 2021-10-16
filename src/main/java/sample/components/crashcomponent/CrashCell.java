package sample.components.crashcomponent;

import component.crashlitycs.CrashDialogController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import component.crashlitycs.CrashReport;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

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
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/CrashDialog.fxml")));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CrashDialogController dialogController = fxmlLoader.<CrashDialogController>getController();
        dialogController.setLabels(this.getItem());

        Scene scene = new Scene(parent, 800, 500);
        Stage stage = new Stage();
        stage.setTitle(this.getItem().getIssueTitle());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }



}
