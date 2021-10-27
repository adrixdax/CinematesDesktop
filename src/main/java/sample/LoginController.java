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
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.api.services.plus.PlusScopes;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class LoginController {

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

    @FXML
    private JFXTextField eMail;
    @FXML
    private JFXPasswordField password;

    @FXML
    public Button LoginButton;
    @FXML
    public Button googleLoginButton;

    @FXML
    private void initialize(){
        eMail.setStyle("-fx-text-fill:#1999CE");
        password.setStyle("-fx-text-fill:#1999CE");
    }

    private void sceneTransition(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample.fxml")));
        Stage stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.setScene(new Scene(root, 1280, 720));
        stage.setResizable(false);
        stage.show();
    }

    private boolean deleteTokens(){
        return new java.io.File("./"+TOKENS_DIRECTORY_PATH+"/StoredCredential").delete();
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = LoginController.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
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

    private void showAlert(){
        Platform.runLater(() -> {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Attenzione!");
            alert.setHeaderText("Non sei un admin");
            Optional<ButtonType> result = alert.showAndWait();
            alert.close();
        });
    }


    public void googleLogin(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            try {
                Credential credential = getCredentials(transport);
                Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), JSON_FACTORY, credential)
                        .setApplicationName(APPLICATION_NAME).build();
                Userinfoplus userinfo = oauth2.userinfo().get().execute();
                if (Boolean.parseBoolean(Request.Get("https://ingsw2021-default-rtdb.firebaseio.com/Users/"+new JSONObject(userinfo.toPrettyString()).get("id").toString()+"/isAdmin.json").execute().returnContent().toString().replaceAll("\"",""))) {
                    try {
                        sceneTransition(mouseEvent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    showAlert();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void usernameAndPasswordLogin (MouseEvent mouseEvent){
        if (mouseEvent.getClickCount() == 1) {
            try {
                Content response = Request.Post("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyDfXAvSg5SCSTXUiBNjwzYxwxWp729DU5M")
                        .bodyForm(Form.form().add("email",eMail.getText()).add("password", password.getText())
                                .add("returnSecureToken", String.valueOf(true)).build())
                        .execute().returnContent();
                if (Boolean.parseBoolean(Request.Get("https://ingsw2021-default-rtdb.firebaseio.com/Users/"+new JSONObject(response.toString()).get("localId").toString()+"/isAdmin.json").execute().returnContent().toString())) {
                    try {
                        sceneTransition(mouseEvent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    showAlert();
                }
            } catch (IOException e) {
                e.printStackTrace();
                //pop up -> non sei nessuno
            }
        }
    }



}
