package com.example.user_service.controller;

import com.example.user_service.model.UserDetails;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserDetailControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    HttpHeaders headers = new HttpHeaders();

    @Test
     void updateUserDetailsById() throws JSONException {

        UserDetails user = new UserDetails();
        user.setUserdetId("10cc0256-99a5-4f19-90d7-2c1deb74fb79");
        user.setAddress("string");
        user.setAge(22);
        user.setBloodGroup("string");
        user.setEmergencyContact(765);
        user.setFcmToken("string");
        user.setGender("female");
        user.setLattitude(0);
        user.setLongitude(0);user.setMartialStatus("string");
        user.setPastMedication("string");
        user.setPicPath("string");user.setPincode(9);
        user.setUsercontact(9L);
        user.setWeight(80);
        user.setBio("hi i am yatin");

        HttpEntity<UserDetails> entity = new HttpEntity<>(user);
     try{
        ResponseEntity<UserDetails> response = testRestTemplate.exchange(
                createURLWithPort("/api/userdetails/updateuserdetails/fb2b6415-e6b1-454d-9053-e07c5076858e")
                , HttpMethod.PUT, entity,UserDetails.class);

        String expected
                ="{ \"bio\": \"hi i am yatin\",\"address\": \"string\", \"age\": 22, \"bloodGroup\": \"string\", \"emergencyContact\": 765, \"fcmToken\": \"string\", \"gender\": \"male\", \"lattitude\": 0, \"longitude\": 0, " +
                "\"martialStatus\": \"string\", \"pastMedication\": \"string\", \"picpath\": \"string\"," +
                " \"pincode\": 9, \"usercontact\": 9, \"userdetId\": \"10cc0256-99a5-4f19-90d7-2c1deb74fb79\"," +
                " \"weight\": 80}";
        System.out.println(response.getBody());
        JSONAssert.assertEquals(expected, String.valueOf(response.getBody()), false);
    }
    catch (Exception e){}
    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}