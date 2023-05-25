package dmit2015.faces;

import dmit2015.restclient.Student;
import dmit2015.restclient.StudentMpRestClient;

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

@Named("currentStudentDeleteView")
@ViewScoped
public class StudentDeleteView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private FirebaseLoginSession _firebaseLoginSession;

    @Inject
    @RestClient
    private StudentMpRestClient _studentMpRestClient;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private String editId;

    @Getter
    private Student existingStudent;

    @PostConstruct
    public void init() {
        String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
        existingStudent = _studentMpRestClient.findById(editId, token);
        if (existingStudent == null) {
            Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
        }
    }

    public String onDelete() {
        String nextPage = "";
        try {
            String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
            _studentMpRestClient.delete(editId, token);
            Messages.addFlashGlobalInfo("Delete was successful.");
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Delete not successful.");
        }
        return nextPage;
    }
}