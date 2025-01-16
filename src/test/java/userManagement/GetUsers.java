package userManagement;

import core.StatusCodeEnum;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utility.JsonReader;
import utility.PropertyReader;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class GetUsers {

    private String jsonPlaceHolderBaseUri = "https://jsonplaceholder.typicode.com";
    private String jsonPlaceHolderPostsEndpoint = "/posts";
    private String jsonPlaceHolderCommentEndpoint = "/comments";
    private String reqresBaseUrl = "https://reqres.in/api";
    private String postmanEchoBaseUri = "https://postman-echo.com/";

    @BeforeTest
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

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

    @Test(description = "Path parameter use")
    public void pathParameter() {

        String raceSeasonValue = "2017";
        Response response = given()
                .pathParam("raceSeason", raceSeasonValue)
                .when()
                .get("http://ergast.com/api/f1/{raceSeason}/circuits.json").then().log().all().extract().response();

        assertEquals(response.statusCode(), 200);
        System.out.println(response.body().asString());
    }

    @Test(description = "Form parameter")
    public void formData() {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("foo1", "bar1");
        formParams.put("foo2", "bar2");

        Response response = given()
                .baseUri(postmanEchoBaseUri)
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParams(formParams)
                .when()
                .post("/post")
                .then()
                .statusCode(200)
                .extract().response();


        System.out.println(response.then().log().all());
    }

    @Test(description = "Single header")
    public void addHeader() {

        Response response;

        response = given()
                .baseUri(postmanEchoBaseUri)
                .header("my-sample-header", "Hello this my single header")
                .when()
                .get("/headers")
                .then()
                .statusCode(200)
                .extract().response();
        System.out.println(response.then().log().all());
    }

    @Test(description = "Sending multiple header")
    public void addHeaders() {

        Map<String, String> headers = new HashMap<>();

        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("my-sample-header", "This is second header");

        Response response;
        response = given()
                .baseUri(postmanEchoBaseUri)
                .headers(headers)
                .when()
                .get("/headers")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.then().log().all());
    }

    @Test(description = "Verifying headers from the response")
    public void verifyHeader() {

        Map<String, String> queryString = new HashMap<>();
        queryString.put("foo1", "bar1");
        queryString.put("foo2", "bar2");

        Response response;

        response = given()
                .baseUri(postmanEchoBaseUri)
                .queryParams(queryString)
                .when()
                .get("/response-headers");

        Headers headers = response.headers();
        for(Header current : headers) {
            if(current.getName().contains("Server") && current.getValue().equals("nginx")) {
                System.out.println(current.getName() + " : " + current.getValue());
            }
        }
        System.out.println("Passed verifyHeader test");
    }

    @Test(description = "Verifying single header")
    public void verifySingleHeader() {

        Map<String, String> queryString = new HashMap<>();
        queryString.put("foo1", "bar1");
        queryString.put("foo2", "bar2");

        Response response;

        response = given()
                .baseUri(postmanEchoBaseUri)
                .queryParams(queryString)
                .when()
                .get("/response-headers");

        String value = response.getHeader("Server");
        System.out.println(value);
    }

    @Test(description = "Setting cookies without map")
    public void sendCookie() {

        Response response;

        response = given()
                .baseUri(postmanEchoBaseUri)
                .cookie("foo1", "bar1")
                .cookie("foo2", "bar2")
                .when()
                .get("/cookies/set");

        System.out.println(response.then().log().all());
    }

    @Test(description = "Setting cookies with map")
    public void sendCookieV2() {

        Response response;

        Map<String, String> cookies = new HashMap<>();
        cookies.put("id", "1023");
        cookies.put("session", "2938730kfdj");

        response = given()
                .baseUri(postmanEchoBaseUri)
                .cookies(cookies)
                .when()
                .get("/cookies/set");

        System.out.println(response.then().log().all());
    }

    @Test(description = "Getting single cookies")
    public void getCookie() {
        Response response;
        Map<String, String> cookies = new HashMap<>();
        cookies.put("id", "krim");
        cookies.put("session", "2003");

        response = given()
                .baseUri(postmanEchoBaseUri)
                .cookies(cookies)
                .when()
                .get("/cookies");

        String id = response.getCookie("sails.sid");

        System.out.println(id);
        System.out.println(response.then().log().all());
    }

    @Test(description = "Getting cookies")
    public void getCookies() {
        Response response;
        Map<String, String> cookies = new HashMap<>();
        cookies.put("id", "krim");
        cookies.put("session", "2003");

        response = given()
                .baseUri(postmanEchoBaseUri)
                .cookies(cookies)
                .when()
                .get("/cookies");

        cookies = response.getCookies();

        assertThat(cookies, hasKey("sails.sid"));
        response.then().cookie("sails.sid", notNullValue());
//        response.then().cookie("sails.sid", "hello")''

        for(Map.Entry<String, String> entry : cookies.entrySet()) {
            System.out.println("Printing cookies");
            System.out.println(entry.getValue());
        }
        System.out.println(response.then().log().all());
    }

    @Test(description = "Basic auth")
    public void basicAuthentication() {

        Response response;

        response = given()
                .baseUri(postmanEchoBaseUri)
                .auth()
                .basic("postman", "password")
                .when()
                .get("/basic-auth");

        assertThat(response.statusCode(), equalTo(200));
        System.out.println(response.then().log().all());
        response.then().body("authenticated", equalTo(true));
    }

    @Test(description = "Basic auth")
    public void digestAuthentication() throws IOException, ParseException {

        String user = JsonReader.getTestData("username");
        String password = JsonReader.getTestData("password");

        System.out.println(user + " : " + password);

        Response response;

        response = given()
                .baseUri(postmanEchoBaseUri)
                .auth()
                .basic(user, password)
                .when()
                .get("/digest-auth");

        assertThat(response.statusCode(), equalTo(200));
        System.out.println(response.then().log().all());
        response.then().body("authenticated", equalTo(true));
    }

    @Test(description = "Delete")
    public void deleteData() {

        String baseUrl = PropertyReader.getValue("config.properties", "postmanEchoBaseUrl");
        System.out.println("Base url is : " + baseUrl);
        Response response;

        response = given()
                .baseUri(baseUrl)
                .when()
                .delete("/delete")
                .then()
                .statusCode(StatusCodeEnum.SUCCESS.code)
                .extract().response();
    }

}
