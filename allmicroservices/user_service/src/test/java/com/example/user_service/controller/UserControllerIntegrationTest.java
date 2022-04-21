package com.example.user_service.controller;

import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.pojos.response.UserResponse;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.List;

/**
 * This is a test Class
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    HttpHeaders headers = new HttpHeaders();

    @Test
     void saveUser() {

        UserEntity user = new UserEntity();
        user.setUserId("379dfb3b-4b78-4232-84af-110d406622a6");
        user.setUserName("VSoniBhai");
        user.setEmail("VinaySoniBhai@gmail.com");
        user.setCreatedAt("23/02");
        user.setLastLogin("23/02");
        user.setUserDetails(null);

        HttpEntity<UserEntity> request = new HttpEntity<>(user);
        try {
            ResponseEntity<String> response = testRestTemplate
                    .exchange("http://localhost:" + port +
                                    "/api/v1/user?fcmToken=hsagdjg&picPath=falka",
                            HttpMethod.POST, request, new ParameterizedTypeReference<String>() {
                            });
            String expected =
                    "{\"status\":\"Success\",\"message\":\"User is already present\"," +
                            "\"userentity\":[{\"userId\":\"d35542b3-70ee-4077-86fa-38ff76ada0d1\",\"userName\":\"VSoniBhai\",\"email\":\"VinaySoniBhai@gmail.com\"," +
                            "\"lastLogin\":\"2022/03/24 13:38:04\",\"createdAt\":\"2022/03/24 13:38:04\"," +
                            "\"userDetails\":{\"userdetId\":\"3f2a407c-588e-4a2d-afce-8be3812c7231\",\"bio\":null," +
                            "\"picPath\":\"falka\",\"age\":0,\"fcmToken\":\"hsagdjg\",\"pincode\":0,\"usercontact\":0," +
                            "\"lattitude\":0.0,\"longitude\":0.0,\"address\":null,\"gender\":null,\"bloodGroup\":null,\"martialStatus\":null,\"weight\":0,\"emergencyContact\":0,\"pastMedication\":null}}],\"jwt\":\"\",\"refreshToken\":\"\"}\n";
            System.out.println(response.getBody());
            JSONAssert.assertEquals(expected, String.valueOf(response.getBody()), false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveNewUser() {

        UserEntity user = new UserEntity();
        user.setUserId("768753");
        user.setUserName("karan");
        user.setEmail("karan@gmail.com");
        user.setCreatedAt("13/02");
        user.setLastLogin("03/02");
        user.setUserDetails(null);

        HttpEntity<UserEntity> request = new HttpEntity<>(user);
        try {
            ResponseEntity<String> response = testRestTemplate
                    .exchange("http://localhost:" + port +
                                    "/api/v1/user?fcmToken=hsagderjg&picPath=falkdda",
                            HttpMethod.POST, request, new ParameterizedTypeReference<String>() {
                            });
            String expected =
                    "{\"status\":\"Success\",\"message\":\"Saved user successfully\"," +
                            "\"userentity\":[{," +
                            "\"userName\":\"karan\",\"email\":\"karan@gmail.com\"," +
                            "," +
                            "\"userDetails\":null}],," +
                            "}\n";
            System.out.println(response.getBody());
            JSONAssert.assertEquals(expected, String.valueOf(response.getBody()), false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
     void LoginUser() {
        UserEntity user = new UserEntity();
        user.setUserId("e61ef901-e105-4636-bef6-f664c204a825");
        user.setUserName("Yatin");
        user.setEmail("yatintri@gmail.com");
        user.setUserDetails(null);
        HttpEntity<UserEntity> request = new HttpEntity<>(user);
        try {
            ResponseEntity<String> response = testRestTemplate
                    .exchange("http://localhost:" + port +
                                    "/api/v1/login?email=yatintri@gmail.com",
                            HttpMethod.POST, request, new ParameterizedTypeReference<String>() {
                            });
            String expected =
                    "{\"status\":\"Success\",\"userentity\":[{\"userId\":\"e61ef901-e105-4636-bef6-f664c204a825\",\"userName\":\"Yatin\",\"email\":\"yatintri@gmail.com\",\"lastLogin\":\"2022/03/23 10:59:08\",\"createdAt\":\"2022/03/23 10:59:08\",\"userDetails\":{\"userdetId\":\"a7acd557-5a4f-4a06-a1c0-d099e322a8f9\",\"bio\":null,\"picPath\":\"kalka\"," +
                            "\"age\":0,\"fcmToken\":\"hsagdj\",\"pincode\":0,\"usercontact\":0," +
                            "\"lattitude\":0.0,\"longitude\":0.0,\"address\":null,\"gender\":null," +
                            "\"bloodGroup\":null,\"martialStatus\":null,\"weight\":0,\"emergencyContact\":0," +
                            "\"pastMedication\":null}}]}\n";
            System.out.println(response.getBody());
            JSONAssert.assertEquals(expected, String.valueOf(response.getBody()), false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void testGetUser() {

        List<UserEntity> user =testRestTemplate.getForObject("http://localhost:"+
                        port + "/api/v1/users",
                List.class);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(21,user.size());
    }

    @Test
     void testRetrieveById() throws Exception {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/v1/user?userId=379dfb3b-4b78-4232-84af-110d406622a6")
                , HttpMethod.GET, entity,String.class);

        String expected = "{\"status\":\"OK\",\"userEntityList\":[{\"userId\":\"379dfb3b-4b78-4232-84af-110d406622a6\",\"userName\":\"Soni\",\"email\":\"VinaySoni@gmail.com\",\"lastLogin\":\"2022/02/23 17:13:45\",\"createdAt\":\"2022/02/23 17:13:45\",\"userDetails\":null}],\"medicinesList\":[{\"medicineId\":5,\"startDate\":\"14-04-2022\",\"medicineName\":\"Paracetamol\",\"medicineDes\":\"syrup\",\"days\":\"Mon\",\"endDate\":\"12-03-2022\",\"time\":\"13:07\",\"title\":\"eat 2 times\",\"totalMedReminders\":6,\"currentCount\":3}]}\n";
        System.out.println(response.getBody());
        JSONAssert.assertEquals(expected,response.getBody(), false);
    }

    @Test
     void testRetrieveByEmail() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        try {
            ResponseEntity<String> response = testRestTemplate.exchange(
                    createURLWithPort("/api/v1/email?email=VinaySoni@gmail.com&sender=yatin@gmail.com")
                    , HttpMethod.GET, entity, String.class);

            String expected
                    = "{\"userId\":\"379dfb3b-4b78-4232-84af-110d406622a6\"," +
                    "\"userName\":\"Soni\",\"email\":\"VinaySoni@gmail.com\"," +
                    "\"lastLogin\":\"2022/02/23 17:13:45\"," +
                    "\"createdAt\":\"2022/02/23 17:13:45\"}";
            System.out.println(response.getBody());
            JSONAssert.assertEquals(expected, response.getBody(), false);
        }
        catch (Exception e){

        }
    }
    @Test
    void testNewEmail() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        try {
            ResponseEntity<String> response = testRestTemplate.exchange(
                    createURLWithPort("/api/v1/email?email=niitya@gmail.com&sender=VinaySoni@gmail.com")
                    , HttpMethod.GET, entity, String.class);

            String expected
                    = "Invitation sent to user with given email id!\n";
            System.out.println(response.getBody());
            JSONAssert.assertEquals(expected, response.getBody(), false);
        }
        catch (Exception e){

        }
    }

    @Test
    void testSendPdf() throws Exception {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/v1/pdf?medId=12345")
                , HttpMethod.GET, entity,String.class);

        String expected = "{\n" +
                "    \"status\": \"Success\",\n" +
                "    \"userentity\": null,\n" +
                "    \"jwt\": \"\",\n" +
                "    \"refreshToken\": \"\"\n" +
                "}";
        System.out.println(response.getBody());
        JSONAssert.assertEquals(expected, String.valueOf(response.getBody()), false);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}

