package sample;

import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import component.films.Film;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.components.crashcomponent.CrashCell;
import component.crashlitycs.CrashReport;
import sample.components.reportcomponent.ReportCell;
import sample.components.reportcomponent.ReportController;
import sample.components.reportcomponent.ReportedReviews;
import sample.retrofit.RetrofitListInterface;
import sample.retrofit.RetrofitResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleController {

    @FXML
    public Label MostSeenFilmLabel;
    @FXML
    public Label onlineUsersLabel;
    @FXML
    public ImageView MostSeenFilmImage;
    @FXML
    public Label MostReviewedFilmLabel;
    @FXML
    public ImageView MostReviewedFilmImage;
    @FXML
    public Label PreferedFilmLabel;
    @FXML
    public ImageView PreferedFilmImage;
    @FXML
    private ListView<String> DeviceList;
    @FXML
    private ListView<String> regions;
    @FXML
    private ListView<ReportedReviews> reportedElements;
    @FXML
    private ListView<CrashReport> crashReport;

    public class mostReviewedClass implements RetrofitListInterface{
        private List<Film> mostReviewed = new ArrayList<>();
        private void getMostReviewedFilms() {
            RetrofitResponse.getResponse("Type=PostRequest&mostreviewed=true", mostReviewedClass.this, "getFilm");
        }

        @Override
        public void setList(List<?> newList) {
            if (mostReviewed.size() == 0) {
                mostReviewed = (List<Film>) newList;
                Platform.runLater(() -> {
                    MostReviewedFilmLabel.setText(mostReviewed.get(0).getFilm_Title() + "\nRecensito da: " + mostReviewed.get(0).getCounter() + (mostReviewed.get(0).getCounter() == 1 ? " utente" : " utenti"));
                    MostReviewedFilmImage.setImage(new Image(mostReviewed.get(0).getPosterPath()));
                });
            }
        }
    }

    public class mostViewedClass implements RetrofitListInterface {
        private List<Film> mostViewed = new ArrayList<>();

        private void getMostViewedFilms() {
            RetrofitResponse.getResponse("Type=PostRequest&mostviewed=true", mostViewedClass.this, "getFilm");
        }

        @Override
        public void setList(List<?> newList) {
            if (mostViewed.size() == 0) {
                mostViewed = (List<Film>) newList;
                Platform.runLater(() -> {
                    MostSeenFilmLabel.setText(mostViewed.get(0).getFilm_Title() + "\nVisto da: " + mostViewed.get(0).getCounter() + (mostViewed.get(0).getCounter() == 1 ? " utente" : " utenti"));
                    MostSeenFilmImage.setImage(new Image(mostViewed.get(0).getPosterPath()));
                });
            }
        }
    }

    public class preferedFilmClass implements RetrofitListInterface {
        private List<Film> preferedFilms = new ArrayList<>();

        private void getPreferedFilm() {
            RetrofitResponse.getResponse("Type=PostRequest&userPrefered=true", preferedFilmClass.this, "getFilm");
        }

        @Override
        public void setList(List<?> newList) {
            if(preferedFilms.size() == 0){
                preferedFilms = (List<Film>) newList;
                Platform.runLater(() ->{
                    PreferedFilmLabel.setText(preferedFilms.get(0).getFilm_Title()+"\nIl preferito di: "+preferedFilms.get(0).getCounter()+(preferedFilms.get(0).getCounter() == 1 ? " utente" : " utenti"));
                    PreferedFilmImage.setImage(new Image(preferedFilms.get(0).getPosterPath()));
                });
            }
        }
    }

    private final mostReviewedClass mostReviewed = new mostReviewedClass();
    private final mostViewedClass mostViewed = new mostViewedClass();
    private final preferedFilmClass preferedFilm = new preferedFilmClass();

    private String onlineUsers = "";


    @FXML
    private synchronized void initialize() {
        ReportController reportController = new ReportController(this);
        mostReviewed.getMostReviewedFilms();
        mostViewed.getMostViewedFilms();
        preferedFilm.getPreferedFilm();
        new Thread(() -> {
            try {
                TableResult tr = BigQuery.getDeviceList();
                for (FieldValueList row : tr.iterateAll())
                    DeviceList.getItems().addAll(row.get(0).getStringValue()+": "+row.get(1).getStringValue());
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }).start();
        getOnlineUsers();
        new Thread(() -> {
            try {
                TableResult tr = BigQuery.getRegions();
                for (FieldValueList row : tr.iterateAll())
                    regions.getItems().addAll(row.get(0).getStringValue()+", "+row.get(1).getStringValue());
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                TableResult tr = BigQuery.getCrashReport();
                crashReport.setCellFactory(crashReportListView -> new CrashCell());
                for (FieldValueList row : tr.iterateAll())
                    crashReport.getItems().add(new CrashReport(row));
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                reportedElements.setCellFactory(studentListView -> new ReportCell());
                reportController.getReportedReviews();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }


    private void getOnlineUsers() {

        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                while (true){
                    RetrofitResponse.getResponse("true", ConsoleController.this, "getOnlineUsers");
                    try{
                        wait(5000);
                    }
                    catch (InterruptedException ex){
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public void setOnlineUsers(Integer onlineUsers) {
        this.onlineUsers = String.valueOf(onlineUsers);
        Platform.runLater(()-> onlineUsersLabel.setText(this.onlineUsers));
    }

    public void updateListView(ReportedReviews r){
        if (!(this.reportedElements.getItems().contains(r))){
            this.reportedElements.getItems().add(r);
        }
    }
}
