package sample;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.api.services.plus.PlusScopes;
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
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class Controller {

    private static final String APPLICATION_NAME = "Client desktop 1";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private final NetHttpTransport transport = new NetHttpTransport();

    /**
     * Global instance of the scopes required by this quickstart. If modifying
     * these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(
            PlusScopes.USERINFO_EMAIL,
            PlusScopes.USERINFO_PROFILE,
            PlusScopes.PLUS_LOGIN);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private Stage stage;
    private Parent root;
    @FXML
    public Button LoginButton;
    @FXML
    public Button googleLoginButton;
    /*    @FXML
        private ... mailTextBox;
        @FXML
        private ... passwordTextBox;
    */
    private void sceneTransition(MouseEvent mouseEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../sample.fxml")));
        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.setScene(new Scene(root, 1280, 720));
        stage.show();
    }

    private boolean deleteTokens(){
        return new java.io.File("./"+TOKENS_DIRECTORY_PATH+"/StoredCredential").delete();
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = Controller.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public void googleLogin(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            try {
                Credential credential = getCredentials(transport);
                Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), JSON_FACTORY, credential)
                        .setApplicationName(APPLICATION_NAME).build();
                Userinfoplus userinfo = oauth2.userinfo().get().execute();
                System.out.println(userinfo.toPrettyString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void usernameAndPasswordLogin (MouseEvent mouseEvent){
        if (mouseEvent.getClickCount() == 1) {
            System.out.println("Ho cliccato");
            try {
                Content response = Request.Post("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyDfXAvSg5SCSTXUiBNjwzYxwxWp729DU5M")
                        .bodyForm(Form.form().add("email","ironman@gmail.com").add("password", "ironman")
                                .add("returnSecureToken", String.valueOf(true)).build())
                        .execute().returnContent();
                System.out.println(response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                testBigQuery();
                test2BigQuery();
                sceneTransition(mouseEvent);
            } catch (InterruptedException | IOException e) {
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
        java.io.File credentialsPath = new java.io.File(Objects.requireNonNull(getClass().getResource("../ingsw2021-bf069d24a538.json")).getPath());
        System.out.println(credentialsPath.getAbsolutePath());
        GoogleCredentials credentials = null;
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BigQuery bigquery = BigQueryOptions.newBuilder().setCredentials(credentials)
                        .setProjectId(projectId).build().getService();
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
                        .setUseLegacySql(false).build();
        System.out.println(queryConfig.getQuery());
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());
        queryJob = queryJob.waitFor();
        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }
        TableResult result = queryJob.getQueryResults();
        for (FieldValueList row : result.iterateAll()) {
            for (FieldValue fieldValue : row) {
                System.out.println(fieldValue.getValue());
            }
        }
    }

}
