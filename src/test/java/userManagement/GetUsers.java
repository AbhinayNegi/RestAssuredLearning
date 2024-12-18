package userManagement;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetUsers {

    @Test
    public void getUserData() {

        given().baseUri("https://reqres.in")
                .when().get("/api/users?page=2")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void validateGetResponseBody() {

        given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .assertThat()
                .body(not(emptyString()))
                .body("userId", equalTo(1))
                .body("id", equalTo(1))
                .body("title", containsString("sunt aut facere"));
    }

}
