package org.example;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.HashMap;

public class MoEngageTestCases {
    public static int bookingId;
    public static String authToken;
    public static HashMap<String, String> map=new HashMap<>();

    @BeforeSuite
    public void setUp(){
        RestAssured.baseURI="https://restful-booker.herokuapp.com";
        Response auth = RestAssured.given().log().all()
                .when()
                .header(new Header("content-Type","application/json"))
                .body("{\n" +
                        "    \"username\": \"admin\",\n" +
                        "    \"password\": \"password123\"\n" +
                        "}")
                .post("auth")
                .then()
                .extract()
                .response();
        JsonPath path = auth.jsonPath();
        authToken= path.get("token");

    }
    @BeforeMethod
    public void loadHeaders(){
        map.put("content-Type","application/json");
        map.put("Cookie","token="+authToken);
    }

    @Test(description = "create a booking")
    public void createBooking(){
        // set the URI
        // set the headers, params...
        //load the payload
        //then perform action - GET/ POST/ PUT
        // extract response, validate



        Response response = RestAssured.given().log().all()
                .headers(map)
                .and()
                .body(requestBody())
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();
        JsonPath jsonPath = response.jsonPath();
        Number price = jsonPath.get("booking.totalprice");
        System.out.println(price);
        int status = response.statusCode();
        System.out.println(status);
        bookingId=jsonPath.get("bookingid");

    }
    @Test(dependsOnMethods = "createBooking")
    public void updateBooking(){
        Response updateBooking = RestAssured.given().log().all()
                .headers(map)
                .and()
                .body(requestBody())
                .when()
                .put("booking/"+bookingId)
                .then()
                .statusCode(200)
                .extract()
                .response();
        JsonPath jsonPath = updateBooking.jsonPath();
        Number totalprice = jsonPath.get("totalprice");
        System.out.println(totalprice);
    }

    public String requestBody(){
        return "{\n" +
                "    \"firstname\" : \"Jim\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";
    }
}
