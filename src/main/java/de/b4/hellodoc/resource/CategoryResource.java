package de.b4.hellodoc.resource;

import de.b4.hellodoc.model.Category;
import de.b4.hellodoc.service.CategoryService;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@ApplicationScoped
@Path("category")
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "Category", description = "Operations for categories")
public class CategoryResource {

  private static final Logger LOGGER = Logger.getLogger(CategoryResource.class.getName());

  @Inject
  CategoryService categoryService;

  @GET
  @Operation(operationId = "getAllCategories",
    summary = "get all categories",
    description = "This operation retrieves categories from the database"
  )
  public Category[] get() {
    return categoryService.getAll();
  }

  @GET
  @Path("{id}")
  @Operation(operationId = "getCategoryById",
    summary = "get the category with the specified id",
    description = "This operation retrieves the category with the specified id from the database"
  )
  @APIResponses(value = {
    @APIResponse(responseCode = "200", description = "returns the category."),
    @APIResponse(responseCode = "404", description = "category not found.")
  })
  public Category get(@PathParam("id") Long id) {
    Category entity = categoryService.getById(id);
    if (entity == null) {
      throw new WebApplicationException("Category with id of " + id + " does not exist.", 404);
    }
    return entity;
  }

  @POST
  @Operation(operationId = "createCategory",
    summary = "creates a category",
    description = "This operation creates a category from the given data"
  )
  @APIResponses(value = {
    @APIResponse(responseCode = "201", description = "Category has been created"),
    @APIResponse(responseCode = "422", description = "id was not empty")
  })
  public Response create(Category category) {
    if (category.getId() != null) {
      throw new WebApplicationException("Id was invalidly set on request.", 422);
    }
    category = categoryService.create(category);
    return Response.ok(category).status(201).build();
  }

  @PUT
  @Operation(operationId = "updateCategory",
    summary = "updates a category",
    description = "This operation updates the category with the given id from the given data"
  )
  @APIResponses(value = {
    @APIResponse(responseCode = "200", description = "Update was successful"),
    @APIResponse(responseCode = "404", description = "category not found."),
    @APIResponse(responseCode = "422", description = "id was not set")
  })
  @Path("{id}")
  public Category update(@PathParam("id") Long id, Category category) {
    if (category.getName() == null) {
      throw new WebApplicationException("Category Name was not set on request.", 422);
    }

    Category existingCategory = categoryService.getById(id);
    if (existingCategory.getId() == null) {
      throw new WebApplicationException("Category with id=" + id + " does not exist.", 404);
    }
    category.setId(id);
    return categoryService.update(category);
  }

  @DELETE
  @Path("{id}")
  @Operation(operationId = "deleteCategory",
    summary = "deletes a category",
    description = "This operation deletes the category with the given id from the database"
  )
  @APIResponses(value = {
    @APIResponse(responseCode = "204", description = "Category was deleted"),
    @APIResponse(responseCode = "404", description = "category not found.")
  })
  public Response delete(@PathParam("id") Long id) {
    Category entity = categoryService.getById(id);
    if (entity == null) {
      throw new WebApplicationException("Category with id of " + id + " does not exist.", 404);
    }
    categoryService.deleteCategory(entity);
    return Response.status(204).build();
  }

  @Provider
  public static class ErrorMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
      LOGGER.error("Failed to handle request", exception);

      int code = 500;
      if (exception instanceof WebApplicationException) {
        code = ((WebApplicationException) exception).getResponse().getStatus();
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
