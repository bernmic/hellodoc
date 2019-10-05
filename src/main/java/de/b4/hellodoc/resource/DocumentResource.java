package de.b4.hellodoc.resource;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.b4.hellodoc.service.LuceneFulltextService;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.b4.hellodoc.model.Document;
import de.b4.hellodoc.model.DocumentType;

import java.io.File;
import java.util.List;

@Path("document")
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "Document", description = "Operations for documents")
public class DocumentResource {

  @Inject
  LuceneFulltextService fulltextService;

  @GET
  @Operation(operationId = "getAllDocuments",
          summary = "get all documents",
          description = "This operation retrieves documents from the database"
  )
  public Document[] get(@QueryParam(value = "filter") String filter) {
    List<Document> documents;
    if (filter != null) {
       documents = fulltextService.findDocuments(filter);
    }
    else {
      documents = Document.listAll();
    }
    return documents.toArray(new Document[0]);
  }

  @GET
  @Path("{id}")
  @Operation(operationId = "getDocumentById",
          summary = "get the document with the specified id",
          description = "This operation retrieves the document with the specified id from the database"
  )
  @APIResponses(value = {
          @APIResponse(responseCode = "200", description = "returns the document."),
          @APIResponse(responseCode = "404", description = "document not found.")
  })
  public Document get(@PathParam("id") Long id) {
    Document entity = Document.findById(id);
    if (entity == null) {
      throw new WebApplicationException("Document with id of " + id + " does not exist.", 404);
    }
    return entity;
  }

  @GET
  @Path("{id}/download")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response downloadDocument(@PathParam("id") Long id) {
    Document entity = Document.findById(id);
    if (entity == null) {
      throw new WebApplicationException("Document with id of " + id + " does not exist.", 404);
    }
    File f = new File(entity.path);
    if (!f.exists()) {
      throw new WebApplicationException("File " + entity.path + " does not exist.", 404);
    }
    Response.ResponseBuilder responseBuilder = Response.ok(f);
    responseBuilder.header("Content-Disposition", "attachment;filename=" + FilenameUtils.getName(entity.path));
    return responseBuilder.build();
  }

  @POST
  @Operation(operationId = "createDocument",
          summary = "creates a document",
          description = "This operation creates a document from the given data"
  )
  @APIResponses(value = {
          @APIResponse(responseCode = "201", description = "Document has been created"),
          @APIResponse(responseCode = "422", description = "id was not empty")
  })
  @Transactional
  public Response create(Document document) {
    if (document.id != null) {
      throw new WebApplicationException("Id was invalidly set on request.", 422);
    }
    if (document.documentType == null || document.name == null || document.path == null) {
      throw new WebApplicationException("Name, Path and DocumentType need to be set", 422);
    }
    DocumentType existingDocumentType = DocumentType.findById(document.documentType.extension);
    if (existingDocumentType == null) {
      // create new document type
      document.documentType.persist();
    } else {
      document.documentType = existingDocumentType;
    }
    document.persist();
    return Response.ok(document).status(201).build();
  }

  @PUT
  @Operation(operationId = "updateDocument",
          summary = "updates a document",
          description = "This operation updates the document with the given id from the given data"
  )
  @APIResponses(value = {
          @APIResponse(responseCode = "200", description = "Update was successful"),
          @APIResponse(responseCode = "404", description = "document not found."),
          @APIResponse(responseCode = "422", description = "id was not set")
  })
  @Path("{id}")
  @Transactional
  public Document update(@PathParam("id") Long id, Document document) {
    if (document.name == null || document.path == null) {
      throw new WebApplicationException("Document Name or path was not set on request.", 422);
    }

    Document existingDocument = Document.findById(id);
    if (existingDocument == null) {
      throw new WebApplicationException("Document with id=" + id + " does not exist.", 404);
    }
    existingDocument.name = document.name;
    existingDocument.path = document.path;
    existingDocument.documentType = document.documentType;
    existingDocument.persist();
    return existingDocument;
  }

  @DELETE
  @Path("{id}")
  @Operation(operationId = "deleteDocument",
          summary = "deletes a document",
          description = "This operation deletes the document with the given id from the database"
  )
  @APIResponses(value = {
          @APIResponse(responseCode = "204", description = "Document was deleted"),
          @APIResponse(responseCode = "404", description = "Document not found.")
  })
  @Transactional
  public Response delete(@PathParam("id") Long id) {
    Document entity = Document.findById(id);
    if (entity == null) {
      throw new WebApplicationException("Document with id of " + id + " does not exist.", 404);
    }
    entity.delete();
    return Response.status(204).build();
  }
}