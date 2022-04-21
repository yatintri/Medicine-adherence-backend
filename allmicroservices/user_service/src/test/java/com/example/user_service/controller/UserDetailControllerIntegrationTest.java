package com.example.user_service.controller;

import com.example.user_service.model.UserDetails;
import com.example.user_service.pojos.dto.UserDetailsDTO;
import com.example.user_service.pojos.response.UserDetailResponse;
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

        UserDetailsDTO user = new UserDetailsDTO();
       // user.setUserdetId("2a36ce7f-1612-4680-8458-ef0da000b6dd");
        user.setAge(21);
        user.setBloodGroup("o+");
        user.setGender("male");
        user.setMartialStatus(null);
        user.setUsercontact(7653L);
        user.setWeight(75);
        user.setBio("hi");

        HttpEntity<UserDetailsDTO> entity = new HttpEntity<>(user);
     try{
        ResponseEntity<UserDetailResponse> response = testRestTemplate.exchange(
                createURLWithPort("/api/v1/user-details?userId=60b9929b-f72d-4c5b-b0b9-328289a2e426")
                , HttpMethod.PUT, entity,UserDetailResponse.class);

        String expected
                ="{\n" +
                "    \"status\": \"Success\",\n" +
                "    \"message\": \"Saved user details\",\n" +
                "    \"userDetails\": {\n" +
                "        \"userdetId\": \"2a36ce7f-1612-4680-8458-ef0da000b6dd\",\n" +
                "        \"bio\": \"hi\",\n" +
                "        \"picPath\": \"DDSAFAASd99980\",\n" +
                "        \"age\": 21,\n" +
                "        \"fcmToken\": \"kaskajdlkjkl\",\n" +
                "        \"pincode\": 0,\n" +
                "        \"usercontact\": 7653,\n" +
                "        \"lattitude\": 0.0,\n" +
                "        \"longitude\": 0.0,\n" +
                "        \"address\": null,\n" +
                "        \"gender\": \"male\",\n" +
                "        \"bloodGroup\": \"o+\",\n" +
                "        \"martialStatus\": null,\n" +
                "        \"weight\": 75,\n" +
                "        \"emergencyContact\": 0,\n" +
                "        \"pastMedication\": null\n" +
                "    }\n" +
                "}";
        System.out.println(response.getBody());
        JSONAssert.assertEquals(expected, String.valueOf(response.getBody()), false);
    }
    catch (Exception e){}
    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}