package dmit2015.faces;

import dmit2015.restclient.TodoItemMultiUser;
import dmit2015.restclient.TodoItemMultiUserMpRestClient;

import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("currentTodoItemMultiUserCreateView")
@RequestScoped
public class TodoItemMultiUserCreateView {

    @Inject
    private LoginSession _loginSession;
    @Inject
    @RestClient
    private TodoItemMultiUserMpRestClient _todoItemMultiUserMpRestClient;

    @Getter
    private TodoItemMultiUser newTodoItemMultiUser = new TodoItemMultiUser();

    public String onCreateNew() {
        String nextPage = null;
        try {
            Response response = _todoItemMultiUserMpRestClient.create(newTodoItemMultiUser, _loginSession.getAuthorization());
            String location = response.getHeaderString("Location");
            String idValue = location.substring(location.lastIndexOf("/") + 1);
            newTodoItemMultiUser = new TodoItemMultiUser();
            Messages.addFlashGlobalInfo("Create was successful. {0}", idValue);
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}