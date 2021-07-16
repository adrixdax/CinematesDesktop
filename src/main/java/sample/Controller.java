package sample;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Controller {

    private Stage stage;
    private Parent root;
    @FXML
    public Button LoginButton;

    public void usernameAndPasswordLogin (MouseEvent mouseEvent){
        if (mouseEvent.getClickCount() == 1) {
            System.out.println("Ho cliccato");
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../sample.fxml")));
                stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
                stage.setScene(new Scene(root, 1280, 720));
                stage.show();
                try {
                    testBigQuery();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String dateToBigQueryFormat(long date){
        return new SimpleDateFormat("yyyyMMdd").format(new Date(date));
    }

    private String dateToBigQueryFormat(Date date){
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    public void testBigQuery() throws InterruptedException, FileNotFoundException {
        System.out.println(dateToBigQueryFormat(new Date(System.currentTimeMillis())));
        System.out.println(dateToBigQueryFormat(System.currentTimeMillis()));
        String projectId = "ingsw2021";
        File credentialsPath = new File(Objects.requireNonNull(getClass().getResource("../ingsw2021-bf069d24a538.json")).getPath());
        System.out.println(credentialsPath.getAbsolutePath());
        // Load credentials from JSON key file. If you can't set the GOOGLE_APPLICATION_CREDENTIALS
        // environment variable, you can explicitly load the credentials file to construct the
        // credentials.
        GoogleCredentials credentials = null;
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BigQuery bigquery =
                BigQueryOptions.newBuilder()
                        .setCredentials(credentials)
                        .setProjectId(projectId)
                        .build()
                        .getService();
        // Use the client.
        System.out.println("Datasets:");
        for (Dataset dataset : bigquery.listDatasets().iterateAll()) {
            System.out.printf("%s%n", dataset.getDatasetId().getDataset());
        }
    }

}
