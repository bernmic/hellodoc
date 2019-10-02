package de.b4.hellodoc.configuration;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.jboss.logging.Logger;

@ApplicationPath("/api")
@OpenAPIDefinition(info = @Info(
        title = "Super duper Document Management System", 
        version = "1.0.0", 
        contact = @Contact(
                name = "Michael Bernards", 
                email = "micael.bernards@b4.de",
                url = "http://www.b4.de")
        ),
        servers = {
            @Server(url = "/",description = "localhost")
        }
)
public class ApiConfiguration extends Application {
        static final Logger log = Logger.getLogger(ApiConfiguration.class.getName());
        
        @PostConstruct
        public void init() {
                log.info("Init...");
        }
}