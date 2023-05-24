package dmit2015.faces;

import dmit2015.restclient.TodoItem;
import dmit2015.restclient.TodoItemMpRestClient;

import jakarta.json.JsonObject;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.LocalDateTime;

@Named("currentTodoItemCreateView")
@RequestScoped
public class TodoItemCreateView {

    @Inject
    @RestClient
    private TodoItemMpRestClient _todoitemMpRestClient;

    @Inject
    private FirebaseLoginSession _firebaseLoginSession;

    @Getter
    private TodoItem newTodoItem = new TodoItem();

    public String onCreateNew() {
        String nextPage = null;
        try {
            String token = _firebaseLoginSession.getToken();
            String userUID = _firebaseLoginSession.getUserUID();

            newTodoItem.setCreateTime(LocalDateTime.now());
            JsonObject responseBody = _todoitemMpRestClient.create(userUID,newTodoItem,token);

            String documentKey = responseBody.getString("name");
            newTodoItem = new TodoItem();
            Messages.addFlashGlobalInfo("Create was successful. {0}", documentKey);
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}