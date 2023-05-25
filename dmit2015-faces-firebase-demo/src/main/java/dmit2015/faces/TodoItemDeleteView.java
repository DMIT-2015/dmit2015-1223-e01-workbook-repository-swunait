package dmit2015.faces;

import dmit2015.restclient.TodoItem;
import dmit2015.restclient.TodoItemMpRestClient;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("currentTodoItemDeleteView")
@ViewScoped
public class TodoItemDeleteView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private TodoItemMpRestClient _todoitemMpRestClient;

    @Inject
    private FirebaseLoginSession _firebaseLoginSession;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private String editId;

    @Getter
    private TodoItem existingTodoItem;

    @PostConstruct
    public void init() {
        String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
        String userUID = _firebaseLoginSession.getFirebaseUser().getLocalId();

        existingTodoItem = _todoitemMpRestClient.findById(userUID, editId, token);
        if (existingTodoItem == null) {
            Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
        }
    }

    public String onDelete() {
        String nextPage = "";
        try {
            String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
            String userUID = _firebaseLoginSession.getFirebaseUser().getLocalId();

            _todoitemMpRestClient.delete(userUID, editId, token);
            Messages.addFlashGlobalInfo("Delete was successful.");
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Delete not successful.");
        }
        return nextPage;
    }
}