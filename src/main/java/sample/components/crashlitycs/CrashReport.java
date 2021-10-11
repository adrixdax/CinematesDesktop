package sample.components.crashlitycs;

import com.google.cloud.bigquery.FieldValueList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.lang.reflect.Field;

public class CrashReport {

    private String issueTitle;
    private String issueSubtitle;
    private String eventTimestamp;
    private String operatingSystemDisplayVersion;
    private String applicationDisplayVersion;
    private String exceptionType;

    public CrashReport(FieldValueList fvl){
        this.issueTitle = fvl.get(0).getStringValue();
        this.issueSubtitle = fvl.get(1).getStringValue();
        this.eventTimestamp = fvl.get(2).getStringValue();
        this.operatingSystemDisplayVersion = fvl.get(3).getStringValue();
        this.applicationDisplayVersion = fvl.get(4).getStringValue();
        this.exceptionType = fvl.get(5).getStringValue();

    }


    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getIssueSubtitle() {
        return issueSubtitle;
    }

    public void setIssueSubtitle(String issueSubtitle) {
        this.issueSubtitle = issueSubtitle;
    }

    public String getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(String eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getOperatingSystemDisplayVersion() {
        return operatingSystemDisplayVersion;
    }

    public void setOperatingSystemDisplayVersion(String operatingSystemDisplayVersion) {
        this.operatingSystemDisplayVersion = operatingSystemDisplayVersion;
    }

    public String getApplicationDisplayVersion() {
        return applicationDisplayVersion;
    }

    public void setApplicationDisplayVersion(String applicationDisplayVersion) {
        this.applicationDisplayVersion = applicationDisplayVersion;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }
}
