package sample.components.reportcomponent;

import component.db.Report;
import component.db.Reviews;
import sample.ConsoleController;
import sample.retrofit.RetrofitListInterface;
import sample.retrofit.RetrofitResponse;

import java.util.*;

public class ReportController implements RetrofitListInterface {

    private final List<Report> reportReviews = new ArrayList<>();
    private final Set<Reviews> reviews = new HashSet<>();
    private final List<ReportedReviews> reportedReviews = new ArrayList<>();
    private ConsoleController cc;

    public ReportController(ConsoleController cc){
        this.cc = cc;
    }

    private void getReport() {
        RetrofitResponse.getResponse("true", ReportController.this, "getReports");
    }

    private void getReviews() {
        for (Report report : reportReviews) {
            RetrofitResponse.getResponse(String.valueOf(report.getId_recordRef()), ReportController.this, "getSingleReview");
        }
    }

    private void buildList(){
        for (Report report : reportReviews) {
            Iterator<Reviews> it = reviews.iterator();
            while (it.hasNext()) {
                Reviews r = it.next();
                if (r.getIdReviews() == report.getId_recordRef()) {
                    cc.updateListView(new ReportedReviews(r, report));
                    synchronized (reviews) {
                        it.remove();
                        reviews.notify();
                    }
                    break;
                }
            }
        }
    }

    public void getReportedReviews() {
        getReport();
    }


    @Override
    public void setList(List<?> newList) {
        if (newList.get(0) instanceof Report) {
            if (reportReviews.size() != 0){
                reportReviews.clear();
            }
            reportReviews.addAll((Collection<? extends Report>) newList);
            getReviews();
        } else {
            synchronized (reviews) {
                reviews.addAll((Collection<? extends Reviews>) newList);
                reviews.notify();
            }
            try {
                buildList();
            }
            catch (Exception ignored){

            }
        }
    }
}