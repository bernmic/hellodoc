package de.b4.hellodoc.configuration;

import de.b4.hellodoc.service.DocumentService;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class ApplicationLifecycleBean {
    private static final Logger LOGGER = Logger.getLogger(ApplicationLifecycleBean.class.getName());

    @Inject
    DocumentService documentService;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.debug("The application is starting...");
        documentService.scanInputDirectory();
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.debug("The application is stopping...");
    }
}
