package dmit2015.faces;

import dmit2015.restclient.FirebaseAuthenticationMpRestClient;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonObject;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Messages;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Named
@ViewScoped
public class FirebaseLogin implements Serializable {

    @Inject
    @RestClient
    private FirebaseAuthenticationMpRestClient _loginService;

    @NotBlank(message = "Username value is required.")
    @Getter
    @Setter
    private String username;

    @NotBlank(message = "Password value is required.")
    @Getter
    @Setter
    private String password;

    @Inject
    private FirebaseLoginSession _loginSession;

    @Inject
    @ConfigProperty(name = "FIREBASE_WEBAPIKEY")
    private String firebaserestapiKey;

    @Param
    private String requestURI;

    public String submit() {
        String nextPage = null;
        // https://firebase.google.com/docs/reference/rest/auth#section-sign-in-email-password
        JsonObject credentials = Json.createObjectBuilder()
                .add("email", username)
                .add("password", password)
                .add("returnSecureToken", true)
                .build();
        try {
            JsonObject responsePayload = _loginService.signIn(firebaserestapiKey, credentials);
            String token = responsePayload.getString("idToken");
            String userUID = responsePayload.getString("localId");
            _loginSession.setToken(token);
            _loginSession.setUsername(username);
            _loginSession.setUserUID(userUID);
            if (requestURI != null && !requestURI.isBlank()) {
                nextPage = requestURI + "?faces-redirect=true";
            } else {
                nextPage = "/index?faces-redirect=true";
            }

        } catch (Exception e) {
            Messages.addGlobalError(e.getMessage());
        }

        return nextPage;
    }

}