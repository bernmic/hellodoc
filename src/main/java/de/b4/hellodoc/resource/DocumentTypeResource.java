package de.b4.hellodoc.resource;

import de.b4.hellodoc.model.DocumentType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("documenttype")
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "DocumentType", description = "Operations for document types")
public class DocumentTypeResource extends BaseResource {

  @GET
  @Operation(operationId = "getAllDocumentTypes",
    summary = "get all document types",
    description = "This operation retrieves document types from the database"
  )
  public DocumentType[] get() {
    return DocumentType.listAll().toArray(new DocumentType[0]);
  }

  @GET
  @Path("{id}")
  @Operation(operationId = "getDocumentTypeByExtension",
    summary = "get the documentType with the specified id",
    description = "This operation retrieves the documentType with the specified id from the database"
  )
  @APIResponses(value = {
    @APIResponse(responseCode = "200", description = "returns the documentType."),
    @APIResponse(responseCode = "404", description = "documentType not found.")
  })
  public DocumentType get(@PathParam("id") Long id) {
    DocumentType entity = DocumentType.findById(id);
    if (entity == null) {
      throw new WebApplicationException("DocumentType with extension " + id + " does not exist.", 404);
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
  @Transactional
  public Response create(DocumentType documentType) {
    if (documentType.extension == null || documentType.name == null) {
      throw new WebApplicationException("Extension or name was not set on request.", 422);
    }
    documentType.persist();
    return Response.ok(documentType).status(201).build();
  }

  @PUT
  @Operation(operationId = "updateDocumentType",
    summary = "updates a documentType",
    description = "This operation updates the documentType with the given id from the given data"
  )
  @APIResponses(value = {
    @APIResponse(responseCode = "200", description = "Update was successful"),
    @APIResponse(responseCode = "404", description = "documentType not found."),
    @APIResponse(responseCode = "422", description = "extension or name was not set")
  })
  @Path("{id}")
  @Transactional
  public DocumentType update(@PathParam("id") Long id, DocumentType documentType) {
    if (documentType.extension == null || documentType.name == null) {
      throw new WebApplicationException("Extension or name was not set on request.", 422);
    }

    DocumentType existingDocumentType = DocumentType.findById(id);
    if (existingDocumentType == null) {
      throw new WebApplicationException("DocumentType with id=" + id + " does not exist.", 404);
    }
    existingDocumentType.extension = documentType.extension;
    existingDocumentType.name = documentType.name;
    existingDocumentType.mimetype = documentType.mimetype;
    existingDocumentType.persist();
    return existingDocumentType;
  }

  @DELETE
  @Path("{id}")
  @Operation(operationId = "deleteDocumentType",
    summary = "deletes a documentType",
    description = "This operation deletes the documentType with the given id from the database"
  )
  @APIResponses(value = {
    @APIResponse(responseCode = "204", description = "DocumentType was deleted"),
    @APIResponse(responseCode = "404", description = "documentType not found.")
  })
  @Transactional
  public Response delete(@PathParam("id") Long id) {
    DocumentType entity = DocumentType.findById(id);
    if (entity == null) {
      throw new WebApplicationException("DocumentType with extension " + id + " does not exist.", 404);
    }
    entity.delete();
    return Response.status(204).build();
  }
}
