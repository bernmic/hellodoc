package de.b4.hellodoc.resource;

import de.b4.hellodoc.model.Category;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("category")
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "Category", description = "Operations for categories")
public class CategoryResource extends BaseResource {

  @GET
  @Operation(operationId = "getAllCategories",
    summary = "get all categories",
    description = "This operation retrieves categories from the database"
  )
  public Category[] get() {
    return Category.listAll().toArray(new Category[0]);
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
    Category entity = Category.findById(id);
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
  @Transactional
  public Response create(Category category) {
    if (category.id != null) {
      throw new WebApplicationException("Id was invalidly set on request.", 422);
    }
    category.persist();;
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
  @Transactional
  public Category update(@PathParam("id") Long id, Category category) {
    if (category.name == null) {
      throw new WebApplicationException("Category Name was not set on request.", 422);
    }

    Category existingCategory = Category.findById(id);
    if (existingCategory == null) {
      throw new WebApplicationException("Category with id=" + id + " does not exist.", 404);
    }
    existingCategory.name = category.name;
    existingCategory.description = category.description;
    existingCategory.persist();
    return existingCategory;
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
  @Transactional
  public Response delete(@PathParam("id") Long id) {
    Category entity = Category.findById(id);
    if (entity == null) {
      throw new WebApplicationException("Category with id of " + id + " does not exist.", 404);
    }
    entity.delete();
    return Response.status(204).build();
  }
}
