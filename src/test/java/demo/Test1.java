package demo;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import java.util.ArrayList;

public class Test1 {

    public static String access_token = "";
    SeleniumCode scObj = new SeleniumCode();


    @BeforeSuite
    public void login() {

     //   scObj.loginVAApp();

        given().
                header("Authorization", "Basic Z3Vlc3RfaG91c2VfcHdkOmdoX3NlY3JldA==").
                param("username", "123").
                param("password", "123").
                param("grant_type", "password").
                when().
                post("http://oauth-server-795787960.ap-northeast-2.elb.amazonaws.com/OAuth2-SSO/oauth/token").
                then().
                statusCode(200);

        access_token =
                given().
                        header("Authorization", "Basic Z3Vlc3RfaG91c2VfcHdkOmdoX3NlY3JldA==").
                        param("username", "123").
                        param("password", "123").
                        param("grant_type", "password").
                        when().
                        post("http://oauth-server-795787960.ap-northeast-2.elb.amazonaws.com/OAuth2-SSO/oauth/token").
                        asString();

        access_token = access_token.substring(17, 53);
        System.out.println(access_token);
    }

    @Test (priority = 1)
    public void home() {
        access_token = "Bearer ".concat(access_token);
        System.out.println("access_token : " + access_token);

     //   scObj.homeVAApp();

        given().
                header("Authorization", access_token).
                get("http://application-721436538.ap-northeast-2.elb.amazonaws.com/profile/chargeCode/list").
                then().
                statusCode(200).
                body("message", equalTo("Successfully listed all the charge codes!"));

        // Test Side bar

        given().header("Authorization", access_token).
                when().
                get("http://application-721436538.ap-northeast-2.elb.amazonaws.com/qava/views/directives/sidebarView.html").
                then().
                statusCode(200).
                log();

        // User FMNO ID on Home Page

        given().header("Authorization", access_token).
                when().
                get("http://application-721436538.ap-northeast-2.elb.amazonaws.com/profile/user/me").
                then().
                statusCode(200).
                body("fmnoId", equalTo("123"));
    }

    @Test (priority = 2)
    public void NewBooking(){

         // Date
        // To store the response and date comparision
        Response r =
                given().header("Authorization", access_token).
                        when().
                        get("http://application-721436538.ap-northeast-2.elb.amazonaws.com/va/booking/dates/open-for-booking").
                        then().extract().response();


        int statusValue = r.path("status");
        String messageValue = r.path("message");
        ArrayList<String> date = r.path("datesOpenForBooking");
        System.out.println("statusValue : " + statusValue);
        System.out.println("date : " + date);
        System.out.println("date.size : " + date.size());
        if (date.size() < 1) {

            Assert.assertEquals(SeleniumCode.date, "Sorry, currently no dates are open for booking.");
        }
    }

}
