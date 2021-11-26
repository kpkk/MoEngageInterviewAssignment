package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import requestEntity.Bookingdates;
import requestEntity.RequestEntityBuilder;
import java.text.ParseException;
import java.util.HashMap;

public class RestFulBooker {

    public HashMap<String, String>headers=new HashMap<>();
    public static int bookingId;
    public static String authToken="";
    public RequestEntityBuilder requestEntityBuilder=new RequestEntityBuilder();

    @BeforeSuite
    public void createToken(){
        headers.put("Content-Type","application/json");
        RestAssured.baseURI="https://restful-booker.herokuapp.com";
        Response auth = RestAssured.given().log().all()
                .headers(headers)
                .body(authInfo())
                .post("auth");
        JsonPath path = auth.jsonPath();
        authToken=path.get("token");

    }


    @BeforeMethod
    public void setUp(){
        headers.put("Cookie","token="+authToken);

    }

    @Test
    public void createBooking() throws ParseException {
        Response booking = RestAssured.given().log().all()
                .headers(headers)
                .when()
                .body(requestSpec())
                .post("/booking")
                .then()
                .extract().response();
        JsonPath jsonPath = booking.jsonPath();
        booking.prettyPrint();
        bookingId= jsonPath.get("bookingid");

    }
    @Test(dependsOnMethods = "createBooking")
    public void updateBooking(){
        Response booking = RestAssured.given().log().all()
                .relaxedHTTPSValidation()
                .headers(headers)
                .when()
                .body(updateFormBody())
                .put("booking/"+bookingId)
                .then()
                .extract().response();
        JsonPath jsonPath = booking.jsonPath();
        booking.prettyPrint();
        Assert.assertEquals(jsonPath.get("totalprice"),161);
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
    public String updateFormBody(){
        return "{\n" +
                "    \"firstname\" : \"Jim\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 161,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";
    }
    public RequestEntityBuilder requestSpec() throws ParseException {
        Bookingdates dates=new Bookingdates();
        dates.setCheckin("2021-10-23");
        dates.setCheckout("2021-10-24");
        requestEntityBuilder.setFirstname("kishore");
        requestEntityBuilder.setLastname("T");
        requestEntityBuilder.setTotalprice(161);
        requestEntityBuilder.setDepositpaid(true);
        requestEntityBuilder.setBookingdates(dates);
        requestEntityBuilder.setAdditionalneeds("lunch");
        return requestEntityBuilder;
    }

    public String authInfo(){
        return "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";
    }
}
