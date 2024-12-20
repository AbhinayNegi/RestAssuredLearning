package userManagement;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetUsers {

    private String jsonPlaceHolderBaseUri = "https://jsonplaceholder.typicode.com";
    private String jsonPlaceHolderPostsEndpoint = "/posts";
    private String jsonPlaceHolderCommentEndpoint = "/comments";
    private String reqresBaseUrl = "https://reqres.in/api";

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
                .baseUri(jsonPlaceHolderBaseUri)
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

    @Test
    public void extractingResponse() {

        Response response;

        response = given()
                .baseUri(jsonPlaceHolderBaseUri)
                .when()
                .get("/posts")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        System.out.println(response.jsonPath().getList("title"));
        assertThat(response.jsonPath().getList("title"), hasItem("sunt aut facere repellat provident occaecati excepturi optio reprehenderit"));
    }

    @Test
    public void verifyTheCommentSize() {

        Response response;

        response = given()
                .baseUri(jsonPlaceHolderBaseUri)
                .when()
                .get(jsonPlaceHolderCommentEndpoint)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        assertThat(response.jsonPath().getList(""), hasSize(500));
    }

    @Test
    public void verifyEmailContainsInSpecificOrder() {

        Response response;

        response = given()
                .baseUri(jsonPlaceHolderBaseUri)
                .when()
                .get("/comments?postId=1")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        List<String> expectedEmails = Arrays.asList("Eliseo@gardner.biz", "Jayne_Kuhic@sydney.com", "Nikita@garfield.biz");
        assertThat(response.jsonPath().getList("email"), contains(expectedEmails.toArray(new String[0])));
    }

    @Test
    public void verifySpecificData() {

        Response response;

        response = given()
                .baseUri(reqresBaseUrl)
                .queryParam("page", 2)
                .when()
                .get("/users")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        response.then().body("data[0].id", is(7));
    }

}
