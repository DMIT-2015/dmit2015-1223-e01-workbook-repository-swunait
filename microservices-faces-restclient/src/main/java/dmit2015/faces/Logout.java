package dmit2015.faces;

import org.omnifaces.util.Faces;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;

@Named
@RequestScoped
public class Logout {

    public void submit() throws ServletException {
        Faces.invalidateSession();
        Faces.redirect( Faces.getRequestContextPath() + "/login.xhtml?faces-redirect=true");
    }

}