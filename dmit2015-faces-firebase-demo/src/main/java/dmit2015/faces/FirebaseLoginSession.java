package dmit2015.faces;

import lombok.Getter;
import lombok.Setter;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Utils;

import java.io.Serializable;

@Named("firebaseLoginSession")
@SessionScoped
public class FirebaseLoginSession implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String token;

    @Getter @Setter
    private String userUID;

    @Getter @Setter
    private String email;

    public String checkForToken() {
        String nextPage = null;

        if (token == null) {
            nextPage = "/firebaseLogin?requestURI=" + Utils.encodeURI(Faces.getRequestURI());
        }

        return nextPage;
    }
}