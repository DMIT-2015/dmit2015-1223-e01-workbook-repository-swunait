package dmit2015.resource;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

@ApplicationPath("/restapi")
@LoginConfig(authMethod="MP-JWT", realmName="MP JWT Realm")
public class JaxRsApplication extends Application {

}