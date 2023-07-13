package dmit2015.microservicesjwtdemo;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

@ApplicationPath("/restapi")
@LoginConfig(authMethod="MP-JWT", realmName="MP JWT Realm")
public class HelloApplication extends Application {

}