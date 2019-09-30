package de.b4.hellodoc.resource;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

public class BaseResource {
    static final Logger log = Logger.getLogger(BaseResource.class.getName());

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {
  
      @Override
      public Response toResponse(Exception exception) {
  
        int code = 500;
        if (exception instanceof WebApplicationException) {
          code = ((WebApplicationException) exception).getResponse().getStatus();
          log.warn("Failed to handle request - " + exception.getMessage());
          log.debug("Stacktrace", exception);
        }
        else {
            log.error("Failed to handle request", exception);
        }
  
        JsonObjectBuilder entityBuilder = Json.createObjectBuilder()
                .add("exceptionType", exception.getClass().getName())
                .add("code", code);
  
        if (exception.getMessage() != null) {
          entityBuilder.add("error", exception.getMessage());
        }
  
        return Response.status(code)
                .entity(entityBuilder.build())
                .build();
      }
    }
  
}