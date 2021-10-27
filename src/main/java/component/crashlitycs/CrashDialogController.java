package component.crashlitycs;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class CrashDialogController {

    @FXML
    private Label subtitle,osDisplay,timestamp,appDisplay;

    public void setLabels(CrashReport crashReport){
        Platform.runLater(() -> {
            subtitle.setText(crashReport.getIssueSubtitle());
            osDisplay.setText(crashReport.getOperatingSystemDisplayVersion());
            timestamp.setText(crashReport.getEventTimestamp());
            appDisplay.setText(crashReport.getApplicationDisplayVersion());
        });

    }



}
