package dmit2015.listener;

import common.security.CallerUser;
import common.security.CallerUserRepository;
import dmit2015.entity.TodoItem;
import dmit2015.repository.TodoItemRepository;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class TodoItemApplicationStartupListener implements ServletContextListener {

    @Inject
    private TodoItemRepository _todoItemRepository;

    @Inject
    private CallerUserRepository _callerUserRepository;

    public TodoItemApplicationStartupListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        try {
            CallerUser user1 = new CallerUser();
            user1.setUsername("DLEE@dmit2015.ca");
            _callerUserRepository.add(user1, "Password2015", new String[]{"Sales","IT"});

            CallerUser user2 = new CallerUser();
            user2.setUsername("DAUSTIN@dmit2015.ca");
            _callerUserRepository.add(user2, "Password2015", new String[]{"IT"});
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TodoItem todo1 = new TodoItem();
            todo1.setName("Create JAX-RS demo project");
            todo1.setComplete(true);
            _todoItemRepository.add(todo1);

            TodoItem todo2 = new TodoItem();
            todo2.setName("Run and verify all Integration Test pass");
            todo2.setComplete(false);
            _todoItemRepository.add(todo2);

            TodoItem todo3 = new TodoItem();
            todo3.setName("Create DTO version of TodoResource");
            todo3.setComplete(false);
            _todoItemRepository.add(todo3);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
    }

}