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
import sample.retrofit.RetrofitListInterface;
import sample.retrofit.RetrofitResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleController implements RetrofitListInterface {

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

    private List<Film> mostReviewed = new ArrayList<>();
    private List<Film> mostViewed = new ArrayList<>();
    private List<Film> preferedFilms = new ArrayList<>();
    private String onlineUsers = new String();


    @FXML
    private synchronized void initialize() {
        getMostReviewedFilms();
        try{
            wait(500);
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }
        getMostViewedFilms();
        try{
            wait(500);
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }
        getPreferedFilm();
        try{
            wait(500);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        new Thread(() -> {
            try {
                TableResult tr = BigQuery.getDeviceList();
                for (FieldValueList row : tr.iterateAll())
                    DeviceList.getItems().addAll(row.get(0).getStringValue()+": "+row.get(1).getStringValue());
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }).start();
        try{
            wait(500);
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }
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
                //for (FieldValueList row : tr.iterateAll())
                    //regions.getItems().addAll(row.get(0).getStringValue()+", "+row.get(1).getStringValue());
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }).start();

    }


    private void getMostViewedFilms() {
        RetrofitResponse.getResponse("Type=PostRequest&mostviewed=true", ConsoleController.this, "getFilm");
    }

    private void getMostReviewedFilms() {
        RetrofitResponse.getResponse("Type=PostRequest&mostreviewed=true", ConsoleController.this, "getFilm");
    }

    private void getPreferedFilm() {
        RetrofitResponse.getResponse("Type=PostRequest&userPrefered=true", ConsoleController.this, "getFilm");
    }

    private void getOnlineUsers() {
        RetrofitResponse.getResponse("true", ConsoleController.this, "getOnlineUsers");
    }


    @Override
    public void setList(List<?> newList) {
        if (mostReviewed.size() == 0) {
            mostReviewed = (List<Film>) newList;
            Platform.runLater(() ->{
                MostReviewedFilmLabel.setText(mostReviewed.get(0).getFilm_Title()+"\nRecensito da: "+mostReviewed.get(0).getCounter()+(mostReviewed.get(0).getCounter() == 1 ? " utente" : " utenti"));
                MostReviewedFilmImage.setImage(new Image(mostReviewed.get(0).getPosterPath()));
            });
        } else if (mostViewed.size() == 0) {
            mostViewed = (List<Film>) newList;
            Platform.runLater(() ->{
                MostSeenFilmLabel.setText(mostViewed.get(0).getFilm_Title()+"\nVisto da: "+mostViewed.get(0).getCounter()+(mostViewed.get(0).getCounter() == 1 ? " utente" : " utenti"));
                MostSeenFilmImage.setImage(new Image(mostViewed.get(0).getPosterPath()));
            });
        } else if(preferedFilms.size() == 0){
            preferedFilms = (List<Film>) newList;
            Platform.runLater(() ->{
                PreferedFilmLabel.setText(preferedFilms.get(0).getFilm_Title()+"\nIl preferito di: "+preferedFilms.get(0).getCounter()+(preferedFilms.get(0).getCounter() == 1 ? " utente" : " utenti"));
                PreferedFilmImage.setImage(new Image(preferedFilms.get(0).getPosterPath()));
            });
        }

    }

    public void setOnlineUsers(Integer onlineUsers) {
        this.onlineUsers = String.valueOf(onlineUsers);
        Platform.runLater(()->{
            onlineUsersLabel.setText(this.onlineUsers);
        });
    }
}
