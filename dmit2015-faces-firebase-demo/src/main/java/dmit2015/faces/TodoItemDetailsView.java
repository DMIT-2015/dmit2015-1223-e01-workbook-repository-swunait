package dmit2015.faces;

import dmit2015.restclient.TodoItem;
import dmit2015.restclient.TodoItemMpRestClient;

import lombok.Getter;
import lombok.Setter;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Faces;

import java.io.Serial;
import java.io.Serializable;

@Named("currentTodoItemDetailsView")
@ViewScoped
public class TodoItemDetailsView implements Serializable {

    @Serial
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
        String token = _firebaseLoginSession.getToken();
        String userUID = _firebaseLoginSession.getUserUID();
        existingTodoItem = _todoitemMpRestClient.findById(userUID, editId, token);
        if (existingTodoItem == null) {
            Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
        }
    }
}