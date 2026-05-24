package security;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

import java.io.InputStreamReader;
import java.util.List;

public final class GoogleOAuthService {

    private static final List<String> SCOPES = List.of(
            "openid",
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/userinfo.profile"
    );

    private GoogleOAuthService() {}

    /**
     * Abre el navegador para que el usuario se autentique con Google.
     * Recibe el código en localhost:8888 y lo intercambia por tokens.
     * Retorna los datos del usuario autenticado.
     */
    public static Userinfo authenticate() throws Exception {
        var transport   = GoogleNetHttpTransport.newTrustedTransport();
        var jsonFactory = GsonFactory.getDefaultInstance();

        // Lee el client_secret.json descargado de Google Cloud Console
        var secrets = GoogleClientSecrets.load(jsonFactory,
                new InputStreamReader(
                        GoogleOAuthService.class.getResourceAsStream("/client_secret.json")
                )
        );

        var flow = new GoogleAuthorizationCodeFlow.Builder(
                transport, jsonFactory, secrets, SCOPES)
                .setAccessType("offline")
                .build();

        // Puerto local donde Google redirige tras autenticar
        var receiver = new LocalServerReceiver.Builder()
                .setPort(8888)
                .build();

        Credential credential =
                new AuthorizationCodeInstalledApp(flow, receiver)
                        .authorize("user");

        Oauth2 oauth2 = new Oauth2.Builder(transport, jsonFactory, credential)
                .setApplicationName("SIS-Colegio")
                .build();

        return oauth2.userinfo().get().execute();
    }
}