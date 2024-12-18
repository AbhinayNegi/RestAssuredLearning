package userManagement;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GetUsers {

    @Test
    public void getUserData() {

        given().baseUri("https://reqres.in")
                .when().get("/api/users?page=2")
                .then()
                .assertThat()
                .statusCode(200);
    }
}
