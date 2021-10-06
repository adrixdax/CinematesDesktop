package sample.reportcomponent;

import component.db.Report;
import component.db.Reviews;
import sample.retrofit.RetrofitListInterface;
import sample.retrofit.RetrofitResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReportController implements RetrofitListInterface {

    private List<Report> reportReviews = new ArrayList<>();
    private List<Reviews> reviews = new ArrayList<>();


    private void getReport() {
        RetrofitResponse.getResponse("true", ReportController.this, "getReports");
    }

    private void getReviews() {
        for (Report report: reportReviews) {
            RetrofitResponse.getResponse(String.valueOf(report.getId_recordRef()), ReportController.this, "getSingleReview");
        }

    }

    public void getReportReviews(){
        getReport();
    }


    @Override
    public void setList(List<?> newList) {
        if(newList.get(0) instanceof  Report) {
            reportReviews = (List<Report>) newList;
            getReviews();
        }else{
            reviews.addAll((Collection<? extends Reviews>) newList);
        }


    }
}