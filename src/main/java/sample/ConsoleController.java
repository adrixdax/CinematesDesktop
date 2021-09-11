package sample;

import javafx.fxml.FXML;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

public class ConsoleController {

    @FXML
    private void initialize() {
        getMostReviewedFilms();
        getMostViewedFilms();
        getPreferedFilm();
        try {
            BigQuery.testBigQuery();
            BigQuery.test2BigQuery();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


    private String executeQuery(Form values) throws IOException {
        String serverLink = "http://cinematesdevelopment.duckdns.org:8080/film";
        return Request.Post(serverLink).bodyForm(values.build()).execute().returnContent().toString();
    }

    private void getMostViewedFilms(){
        new Thread(() -> {
            String response= null;
            try {
                response = executeQuery(Form.form().add("Type","PostRequest")
                        .add("mostviewed", String.valueOf(true)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(response);
        }).start();
    }

    private void getMostReviewedFilms(){
        new Thread(() -> {
            String response= null;
            try {
                response = executeQuery(Form.form().add("Type","PostRequest")
                        .add("mostreviewed", String.valueOf(true)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(response);
        }).start();

    }

    private void getPreferedFilm(){
        new Thread(() -> {
            String response= null;
            try {
                response = executeQuery(Form.form().add("Type","PostRequest")
                        .add("userPrefered", String.valueOf(true)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(response);
        }).start();

    }

}


//Type=PostRequest&mostviewed=true