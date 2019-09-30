package de.b4.hellodoc.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

@QuarkusTest
public class CategoryResourceTest {
    // should match to the initial value of the category sequence
    private final Long FIRST_ID = 1000L;

    @Test
    public void testHelloEndpoint() {

        //Create the Category "Assurance":
        given()
                .when()
                .body("{\"name\" : \"Assurance\", \"description\" : \"Assurance related\"}")
                .contentType("application/json")
                .post("/api/category")
                .then()
                .statusCode(201);

        //Create the Category "Bank":
        given()
                .when()
                .body("{\"name\" : \"Bank\", \"description\" : \"Bank related\"}")
                .contentType("application/json")
                .post("/api/category")
                .then()
                .statusCode(201);

        //Create the Category "Tax":
        given()
                .when()
                .body("{\"name\" : \"Tax\", \"description\" : \"Tax related\"}")
                .contentType("application/json")
                .post("/api/category")
                .then()
                .statusCode(201);

        // get all categories
        given()
          .when().get("/api/category")
          .then()
             .statusCode(200)
             .body(
                     containsString("Assurance"),
                     containsString("Bank"),
                     containsString("Tax")
             );

        // update category with id=1000
        given()
                .when()
                .body("{\"name\" : \"Health\", \"description\" : \"Health related\"}")
                .contentType("application/json")
                .put("/api/category/" + FIRST_ID)
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Assurance")),
                        containsString("Health")
                );

        // get category with id=1000
        given()
                .when().get("/api/category/" + FIRST_ID)
                .then()
                .statusCode(200)
                .body(containsString("Health"));

        // delete category with id=1000
        given()
                .when().delete("/api/category/" + FIRST_ID)
                .then()
                .statusCode(204);

        // test not found
        given()
                .when().get("/api/category/" + FIRST_ID)
                .then()
                .statusCode(404);
    }

}