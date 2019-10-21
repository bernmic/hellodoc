package de.b4.hellodoc.resource;

import de.b4.hellodoc.model.Category;
import de.b4.hellodoc.model.Document;
import de.b4.hellodoc.model.DocumentType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;
import java.util.Map;

@Path("info")
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "Info", description = "Informations about API / hellodoc")
public class InfoResource extends BaseResource {
  @GET
  @Operation(operationId = "capacities",
          summary = "get count of datasets",
          description = "This operation retrieves counts of documents, categories, ..."
  )
  @Path("capacities")
  public Map<String,Long> getCapacities() {
    Map<String,Long> map = new HashMap<>();
    map.put("documents", Document.count());
    map.put("categories", Category.count());
    map.put("documenttypes", DocumentType.count());
    return map;
  }
}
