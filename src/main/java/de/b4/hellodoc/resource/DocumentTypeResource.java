package de.b4.hellodoc.resource;

import de.b4.hellodoc.model.DocumentType;
import de.b4.hellodoc.service.DocumentTypeService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("documenttype")
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "DocumentType", description = "Operations for document types")
public class DocumentTypeResource extends BaseResource {

  @Inject
  DocumentTypeService documentTypeService;

  @GET
  @Operation(operationId = "getAllDocumentTypes",
    summary = "get all document types",
    description = "This operation retrieves document types from the database"
  )
  public DocumentType[] get() {
    return documentTypeService.getAll();
  }

  @GET
  @Path("{extension}")
  @Operation(operationId = "getDocumentTypeByExtension",
    summary = "get the documentType with the specified extension",
    description = "This operation retrieves the documentType with the specified extension from the database"
  )
  @APIResponses(value = {
    @APIResponse(responseCode = "200", description = "returns the documentType."),
    @APIResponse(responseCode = "404", description = "documentType not found.")
  })
  public DocumentType get(@PathParam("extension") String extension) {
    DocumentType entity = documentTypeService.getByExtension(extension);
    if (entity == null) {
      throw new WebApplicationException("DocumentType with extension " + extension + " does not exist.", 404);
    }
    return entity;
  }

  @POST
  @Operation(operationId = "createDocumentType",
    summary = "creates a documentType",
    description = "This operation creates a documentType from the given data"
  )
  @APIResponses(value = {
    @APIResponse(responseCode = "201", description = "DocumentType has been created"),
    @APIResponse(responseCode = "422", description = "extension or name was not set")
  })
  public Response create(DocumentType documentType) {
    if (documentType.getExtension() == null || documentType.getName() == null) {
      throw new WebApplicationException("Extension od name was not set on request.", 422);
    }
    documentType = documentTypeService.create(documentType);
    return Response.ok(documentType).status(201).build();
  }

  @PUT
  @Operation(operationId = "updateDocumentType",
    summary = "updates a documentType",
    description = "This operation updates the documentType with the given extension from the given data"
  )
  @APIResponses(value = {
    @APIResponse(responseCode = "200", description = "Update was successful"),
    @APIResponse(responseCode = "404", description = "documentType not found."),
    @APIResponse(responseCode = "422", description = "extension or name was not set")
  })
  @Path("{extension}")
  public DocumentType update(@PathParam("extension") String extension, DocumentType documentType) {
    if (documentType.getExtension() == null || documentType.getName() == null) {
      throw new WebApplicationException("Extension od name was not set on request.", 422);
    }

    DocumentType existingDocumentType = documentTypeService.getByExtension(extension);
    if (existingDocumentType.getExtension() == null) {
      throw new WebApplicationException("DocumentType with extension=" + extension + " does not exist.", 404);
    }
    return documentTypeService.update(documentType);
  }

  @DELETE
  @Path("{extension}")
  @Operation(operationId = "deleteDocumentType",
    summary = "deletes a documentType",
    description = "This operation deletes the documentType with the given extension from the database"
  )
  @APIResponses(value = {
    @APIResponse(responseCode = "204", description = "DocumentType was deleted"),
    @APIResponse(responseCode = "404", description = "documentType not found.")
  })
  public Response delete(@PathParam("extension") String extension) {
    DocumentType entity = documentTypeService.getByExtension(extension);
    if (entity == null) {
      throw new WebApplicationException("DocumentType with extension " + extension + " does not exist.", 404);
    }
    documentTypeService.delete(entity);
    return Response.status(204).build();
  }
}
