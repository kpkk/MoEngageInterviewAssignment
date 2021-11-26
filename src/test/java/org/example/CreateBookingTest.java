package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import requestEntity.RequestEntityBuilder;
import responseEntity.BookingInfo;

import java.text.ParseException;
import java.util.HashMap;
import java.util.TreeMap;

public class CreateBookingTest {

    public HashMap<String, String> headers=new HashMap<>();
    public static int bookingId;
    public static String authToken="";
    public RequestEntityBuilder requestEntityBuilder=new RequestEntityBuilder();
    @BeforeSuite
    public void createToken(){
        headers.put("Content-Type","application/json");
        RestAssured.baseURI="https://restful-booker.herokuapp.com";
        Response auth = RestAssured.given().log().all()
                .headers(headers)
                .body("authInfo()")
                .post("auth");
        JsonPath path = auth.jsonPath();
        authToken=path.get("token");

    }


   // @BeforeMethod
    public void setUp(){
       // headers.put("Cookie","token="+authToken);

    }

    @Test
    public void createBooking() throws ParseException {
       /* ResponseSpecification responseSpecification = RestAssured.given().log().all()
                .headers(headers).expect().defaultParser(Parser.JSON);*/
        BookingInfo as = RestAssured.given().log().all()
                .headers(headers)
                .when()
                .body(createFormBody())
                .post("/booking")
                .then()
                .extract().response().as(BookingInfo.class);
        System.out.println( as.getBookingDetails());


    }

    public String createFormBody(){
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
