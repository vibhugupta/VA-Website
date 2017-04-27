package demo;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Test1 {

    public static String access_token = "";
    SeleniumCode scObj = new SeleniumCode();


    @BeforeSuite
    public void login() {

        scObj.loginVAApp();

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

    @Test(priority = 1)
    public void home() {
        access_token = "Bearer ".concat(access_token);
        System.out.println("access_token : " + access_token);

        scObj.homeVAApp();

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

    @Test(priority = 2)
    public void NewBooking() {

        scObj.newBooking();
        // Date Logic Starting
        // To store the response and date comparision
        Response responseStartDate =
                given().header("Authorization", access_token).
                        when().
                        get("http://application-721436538.ap-northeast-2.elb.amazonaws.com/va/booking/dates/open-for-booking").
                        then().extract().response();


        ArrayList<String> startingDate = responseStartDate.path("datesOpenForBooking");

        System.out.println("Starting Date First Value : " + startingDate.get(0));
        System.out.println("date.size : " + startingDate.size());

        String startingDateObj = scObj.currentDate();


        if (startingDate.size() < 1) {

            Assert.assertEquals(SeleniumCode.startingDate, null);

        } else {
            Assert.assertEquals(startingDate.get(0), startingDateObj);
            Assert.assertEquals(startingDate.get(0), SeleniumCode.startingDate);
        }

        // Date Logic Ending

        // Charge Code List Starting

        given().
                header("Authorization", access_token).
                get("http://application-721436538.ap-northeast-2.elb.amazonaws.com/profile/chargeCode/list").
                then().
                statusCode(200).
                body("message", equalTo("Successfully listed all the charge codes!"));


        // Charge Code List Ending

        // HighEnd PPt Code Starting
        Response responseHighEndPPT =
                given().
                        header("Authorization", access_token).
                        get("http://application-721436538.ap-northeast-2.elb.amazonaws.com/va/skill/ppt").
                        then().
                        statusCode(200).
                        extract().response();
        String highEndPPTText = responseHighEndPPT.path("skill");
        Assert.assertEquals(highEndPPTText, "Highend PPT");

        Response responseNonPPT =
                given().
                        header("Authorization", access_token).
                        get("http://application-721436538.ap-northeast-2.elb.amazonaws.com/va/skill/nonppt").
                        then().
                        statusCode(200).
                        extract().response();

        ArrayList<String> highEndNonPPTText = responseNonPPT.path("skill");
        int highEndNonPPTSize = highEndNonPPTText.size();
        String[] skillNameFromAPI = new String[highEndNonPPTSize];
      //  System.out.println("highEndNonPPTSize : " + highEndNonPPTSize);
        for (int i = 0; i < highEndNonPPTSize - 1; i++) {
            skillNameFromAPI[i] = highEndNonPPTText.get(i);
            // System.out.println("highEndNonPPTText : " +s[i]);
            Assert.assertEquals(skillNameFromAPI[i], SeleniumCode.skillNameOtherThanPPT[i + 1]);
        }
        //   System.out.println("highEndNonPPTText : " +highEndNonPPTText);


        // HighEnd PPt Code Ending

        // Duration Code Starting
        Response responseDuration =
                given().
                        header("Authorization", access_token).
                        get("http://application-721436538.ap-northeast-2.elb.amazonaws.com/qava/duration.json").
                        then().
                        statusCode(200).
                        extract().response();

        HashMap<String, String> durationHours = new HashMap<String, String>();
        durationHours = responseDuration.path("userData");
        System.out.println("durationHours Size: " + durationHours.size());
        String slotDurationAPI = "";
        for (int i = 1; i <= durationHours.size(); i++) {
            slotDurationAPI = Integer.toString(i);
            System.out.println("durationHours slots : " + durationHours.get(slotDurationAPI));
        }

    }

}
