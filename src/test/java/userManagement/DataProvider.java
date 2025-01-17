package userManagement;

import io.restassured.response.Response;
import org.hamcrest.object.HasEqualValues;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;
import utility.JsonReader;
import utility.PropertyReader;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DataProvider {

    @org.testng.annotations.DataProvider(name = "getLoginCredentials")
    public Object[][] getUserData() {

        return new Object[][] {
                {"1", "George"},
                {"2", "Janet"}
        };
    }

    @Test(dataProvider = "getLoginCredentials", description = "Logging in with different users")
    public void searchUser(String id, String firstName) throws IOException, ParseException {

        String baseUrl = PropertyReader.getValue("config.properties", "reqresBaseUrl");
        String endpoint = JsonReader.getTestData("reqresUsersEndpoint");

        String url = baseUrl + endpoint;
        Response response;
        response = given()
                .pathParam("id", id)
                .when()
                .get(url + "/{id}")
                .then()
                .statusCode(200)
                .extract().response();
        
        System.out.println(response.jsonPath().getString("data.first_name"));
        response.then().assertThat().body("data.first_name", equalTo(firstName));

    }
}
