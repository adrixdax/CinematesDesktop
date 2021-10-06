package component.db;

public class Report {

    private int idReport = 0;
    private String id_user = "";
    private int id_recordRef = 0;
    private String reportType = "";

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
