package dmit2015.faces;

import dmit2015.restclient.TodoItemRbac;
import dmit2015.restclient.TodoItemRbacMpRestClient;
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

@Named("currentTodoItemRbacListView")
@ViewScoped
public class TodoItemRbacListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private TodoItemRbacMpRestClient _todoItemRbacMpRestClient;

    @Getter
    private List<TodoItemRbac> todoItemRbacList;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            todoItemRbacList = _todoItemRbacMpRestClient.findAll();
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}