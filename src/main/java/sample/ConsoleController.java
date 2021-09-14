package sample;

import component.films.Film;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.retrofit.RetrofitListInterface;
import sample.retrofit.RetrofitResponse;

import java.util.ArrayList;
import java.util.List;

public class ConsoleController implements RetrofitListInterface {

    @FXML
    public Label MostSeenFilmLabel;
    @FXML
    public ImageView MostSeenFilmImage;

    private List<Film> mostReviewed = new ArrayList<>();
    private List<Film> mostViewed = new ArrayList<>();
    private List<Film> preferedFilms = new ArrayList<>();

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
        //try {
        //BigQuery.testBigQuery();
        //BigQuery.test2BigQuery();
        //} catch (InterruptedException | IOException e) {
        //    e.printStackTrace();
        //}
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

    @Override
    public void setList(List<?> newList) {
        if (mostReviewed.size() == 0) {
            mostReviewed = (List<Film>) newList;
        } else if (mostViewed.size() == 0) {
            mostViewed = (List<Film>) newList;
            Platform.runLater(() ->{
                MostSeenFilmLabel.setText(mostViewed.get(0).getFilm_Title()+"\n Visto da : "+mostViewed.get(0).getCounter()+(mostViewed.get(0).getCounter() == 0 ? " utente" : " utenti"));
                System.out.println(mostViewed.get(0).getPosterPath());
                MostSeenFilmImage.setImage(new Image(mostViewed.get(0).getPosterPath()));
            });
        } else {
            preferedFilms = (List<Film>) newList;
        }
    }
}


//Type=PostRequest&mostviewed=true