package de.b4.hellodoc.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/hello")
@Tag(name = "Hello", description = "Example operations")
public class HelloResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(operationId = "hello",
        summary = "example call",
        description = "This operation sends just a hello"
    )
    public String hello() {
        return "hello";
    }
}