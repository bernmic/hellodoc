package de.b4.hellodoc.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import de.b4.hellodoc.model.Document;
import de.b4.hellodoc.model.DocumentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

import javax.json.bind.JsonbBuilder;

@QuarkusTest
public class DocumentResourceTest {

    @Test
    public void testDocumentsEndpoints() {
        //Create the Document "Test":
        Document document = new Document();
        document.name = "Test";
        document.path = "/dir/test";
        document.documentType = new DocumentType();
        document.documentType.extension = "xxx";
        document.documentType.name = "Specialtype";
        document.documentType.mimetype = "application/special";
        String documentJson = JsonbBuilder.create().toJson(document);
        given()
                .when()
                .body(documentJson)
                .contentType("application/json")
                .post("/api/document")
                .then()
                .statusCode(201);

        document = Document.findAll().firstResult();

        // get all categories
        given()
          .when().get("/api/document")
          .then()
             .statusCode(200)
             .body(
                     containsString("Test")
             );

        // update category with id=1
        document.name = "Whatever";
        document.path = "/dir/whatever";
        documentJson = JsonbBuilder.create().toJson(document);
        given()
                .when()
                .body(documentJson)
                .contentType("application/json")
                .put("/api/document/" + document.id)
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Test")),
                        containsString("Whatever")
                );

        // get category with id=1
        given()
                .when().get("/api/document/" + document.id)
                .then()
                .statusCode(200)
                .body(containsString("Whatever"));

        // delete category with id=1
        given()
                .when().delete("/api/document/" + document.id)
                .then()
                .statusCode(204);

        // test not found
        given()
                .when().get("/api/document/" + document.id)
                .then()
                .statusCode(404);
    }
}