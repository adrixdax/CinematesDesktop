package sample.components.reportcomponent;

import component.db.Report;
import component.db.Reviews;

public class ReportedReviews {

    private int idReport = 0;
    private String id_user = "";
    private int id_recordRef = 0;
    private String reportType = "";
    private String title;
    private String description;
    private double val;

    public ReportedReviews(Reviews r, Report report) {
        this.idReport=report.getIdReport();
        this.id_user=report.getId_user();
        this.id_recordRef=report.getId_recordRef();
        this.reportType=report.getReportType();
        this.title=r.getTitle();
        this.description=r.getDescription();
        this.val=r.getVal();
    }

    public int getIdReport() {
        return idReport;
    }

    public void setIdReport(int idReport) {
        this.idReport = idReport;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public int getId_recordRef() {
        return id_recordRef;
    }

    public void setId_recordRef(int id_recordRef) {
        this.id_recordRef = id_recordRef;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }
}
