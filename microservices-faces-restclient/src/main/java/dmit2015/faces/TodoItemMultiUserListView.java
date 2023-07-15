package dmit2015.faces;

import dmit2015.restclient.TodoItemMultiUser;
import dmit2015.restclient.TodoItemMultiUserMpRestClient;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named("currentTodoItemMultiUserListView")
@ViewScoped
public class TodoItemMultiUserListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private LoginSession _loginSession;

    @Inject
    @RestClient
    private TodoItemMultiUserMpRestClient _todoItemMultiUserMpRestClient;

    @Getter
    private List<TodoItemMultiUser> todoItemMultiUserList;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            todoItemMultiUserList = _todoItemMultiUserMpRestClient.findAll(_loginSession.getAuthorization());
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}