package component.db;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Report {

    @JsonProperty("idReport")
    private int idReport = 0;
    @JsonProperty("id_user")
    private String id_user = "";
    @JsonProperty("id_recordRef")
    private int id_recordRef = 0;
    @JsonProperty("reportType")
    private String reportType = "";

    public Report(@JsonProperty("idReport") int idReport, @JsonProperty("id_user") String id_user,
                  @JsonProperty("id_recordRef")  int id_recordRef, @JsonProperty("reportType") String reportType) {
        this.idReport = idReport;
        this.id_user = id_user;
        this.id_recordRef = id_recordRef;
        this.reportType = reportType;
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


}
