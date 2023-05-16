package dmit2015.faces;

import dmit2015.restclient.Student;
import dmit2015.restclient.StudentMpRestClient;

import jakarta.json.JsonObject;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("currentStudentCreateView")
@RequestScoped
public class StudentCreateView {

    @Inject
    @RestClient
    private StudentMpRestClient _studentMpRestClient;

    @Getter
    private Student newStudent = new Student();

    public String onCreateNew() {
        String nextPage = null;
        try {
            JsonObject responseBody = _studentMpRestClient.create(newStudent);
            String documentKey = responseBody.getString("name");
            newStudent = new Student();
            Messages.addFlashGlobalInfo("Create was successful. {0}", documentKey);
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}