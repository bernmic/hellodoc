package de.b4.hellodoc.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import de.b4.hellodoc.model.Category;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

@QuarkusTest
public class CategoryResourceTest {

        @Test
    public void testCategoriesEndpoints() {

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

        Category category = Category.find("name", "Assurance").firstResult();

        // update category Assurance
        given()
                .when()
                .body("{\"name\" : \"Health\", \"description\" : \"Health related\"}")
                .contentType("application/json")
                .put("/api/category/" + category.id)
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Assurance")),
                        containsString("Health")
                );

        // get category with id=1000
        given()
                .when().get("/api/category/" + category.id)
                .then()
                .statusCode(200)
                .body(containsString("Health"));

        // delete category with id=1000
        given()
                .when().delete("/api/category/" + category.id)
                .then()
                .statusCode(204);

        // test not found
        given()
                .when().get("/api/category/" + category.id)
                .then()
                .statusCode(404);
    }

}