package de.b4.hellodoc.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

@QuarkusTest
public class DocumentTypeResourceTest {

    @Test
    public void testDocumentTypeEndpoints() {

        //Create the DocumentType "pdf":
        given()
                .when()
                .body("{\"extension\" : \"pdf\", \"name\" : \"PDF\", \"mimetype\" : \"PDF documenttype\"}")
                .contentType("application/json")
                .post("/api/documenttype")
                .then()
                .statusCode(201);

        //Create the DocumentType "doc":
        given()
                .when()
                .body("{\"extension\" : \"doc\", \"name\" : \"DOC\", \"mimetype\" : \"DOC documenttype\"}")
                .contentType("application/json")
                .post("/api/documenttype")
                .then()
                .statusCode(201);

        //Create the DocumentType "xls":
        given()
                .when()
                .body("{\"extension\" : \"xls\", \"name\" : \"XLS\", \"mimetype\" : \"XLS documenttype\"}")
                .contentType("application/json")
                .post("/api/documenttype")
                .then()
                .statusCode(201);

        // get all document types
        given()
          .when().get("/api/documenttype")
          .then()
             .statusCode(200)
             .body(
                     containsString("PDF"),
                     containsString("DOC"),
                     containsString("XLS")
             );

        // update documenttype with extension pdf
        given()
                .when()
                .body("{\"extension\" : \"pdf\", \"name\" : \"FDP\", \"mimetype\" : \"FDP documenttype\"}")
                .contentType("application/json")
                .put("/api/documenttype/pdf")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("PDF")),
                        containsString("FDP")
                );

        // get documenttype with extension pdf
        given()
                .when().get("/api/documenttype/pdf")
                .then()
                .statusCode(200)
                .body(containsString("FDP"));

        // delete documenttype with extension pdf
        given()
                .when().delete("/api/documenttype/pdf")
                .then()
                .statusCode(204);

        // test not found
        given()
                .when().get("/api/documenttype/pdf")
                .then()
                .statusCode(404);
    }

}