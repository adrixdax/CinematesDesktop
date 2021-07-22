package sample;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

public class Controller {

    private Stage stage;
    private Parent root;
    @FXML
    public Button LoginButton;

    public void usernameAndPasswordLogin (MouseEvent mouseEvent){
        if (mouseEvent.getClickCount() == 1) {
            System.out.println("Ho cliccato");
            try {
                //FirebaseAuth mAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance("INGSW2021"));
                //System.out.println(mAuth.toString());
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../sample.fxml")));
                stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
                stage.setScene(new Scene(root, 1280, 720));
                stage.show();
                try {
                    testBigQuery();
                    test2BigQuery();
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

    public void test2BigQuery() throws InterruptedException, IOException {
        BigQuery bigquery = BigQueryOptions.newBuilder().setProjectId("ingsw2021")
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(Objects.requireNonNull(getClass().getResource("../ingsw2021-bf069d24a538.json")).getPath()))).build().getService();
        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(
                       "SELECT user_id, event_name,(SELECT value.string_value FROM UNNEST(event_params)" +
                               "WHERE key = 'firebase_screen_class' and value.string_value=\"ToolBarActivity\") AS class," +
                               "geo.city as city " +
                               "FROM `ingsw2021.analytics_260600984.events_"+dateToBigQueryFormat(System.currentTimeMillis()-(2*24 * 60 * 60 * 1000))+"` WHERE event_name=\"user_engagement\" and geo.city is not null " +
                               "order by event_timestamp desc;")
                        // Use standard SQL syntax for queries.
                        // See: https://cloud.google.com/bigquery/sql-reference/
                        .setUseLegacySql(false)
                        .build();
        System.out.println(queryConfig.getQuery());
// Create a job ID so that we can safely retry.
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

// Wait for the query to complete.
        queryJob = queryJob.waitFor();

// Check for errors
        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            // You can also look at queryJob.getStatus().getExecutionErrors() for all
            // errors, not just the latest one.
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }
        TableResult result = queryJob.getQueryResults();

// Print all pages of the results.
        for (FieldValueList row : result.iterateAll()) {
            for (FieldValue fieldValue : row) {
                System.out.println(fieldValue.getValue());
            }
        }
    }

}
