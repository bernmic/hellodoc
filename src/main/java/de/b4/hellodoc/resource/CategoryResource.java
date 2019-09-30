package de.b4.hellodoc.resource;

import de.b4.hellodoc.model.Category;
import de.b4.hellodoc.service.CategoryService;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@ApplicationScoped
@Path("api/category")
@Produces("application/json")
@Consumes("application/json")
public class CategoryResource {

  private static final Logger LOGGER = Logger.getLogger(CategoryResource.class.getName());

  @Inject
  CategoryService categoryService;

  @GET
  public Category[] get() {
    return categoryService.getAll();
  }

  @GET
  @Path("{id}")
  public Category get(@PathParam("id") Long id) {
    Category entity = categoryService.getById(id);
    if (entity == null) {
      throw new WebApplicationException("Category with id of " + id + " does not exist.", 404);
    }
    return entity;
  }

  @POST
  public Response create(Category category) {
    if (category.getId() != null) {
      throw new WebApplicationException("Id was invalidly set on request.", 422);
    }
    category = categoryService.create(category);
    return Response.ok(category).status(201).build();
  }

  @PUT
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
  @Transactional
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
