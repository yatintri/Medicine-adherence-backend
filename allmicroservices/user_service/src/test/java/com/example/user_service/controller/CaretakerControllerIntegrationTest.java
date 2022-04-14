//package com.example.user_service.controller;
//
//import com.example.user_service.model.UserDetails;
//import com.example.user_service.pojos.caretakerpojos.UserCaretakerpojo;
//import org.json.JSONException;
//import org.junit.jupiter.api.Test;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class CaretakerControllerIntegrationTest {
//
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//
//    @LocalServerPort
//    private int port;
//
//    HttpHeaders headers = new HttpHeaders();
//
//    @Test
//    void testSaveCaretaker() {
//        UserCaretakerpojo user = new UserCaretakerpojo();
//
//        user.setCaretakerId("string");
//        user.setPatientId("string");
//        user.setPatientName("Shubham");
//        user.setReqStatus(true);
//        user.setCaretakerUsername("Piyush");
//        user.setSentBy("Shubham");
//
//
//        HttpEntity<UserCaretakerpojo> request = new HttpEntity<>(user);
//        try {
//            ResponseEntity<String> response = testRestTemplate
//                    .exchange("http://localhost:" + port +
//                                    "/api/caretaker/savecaretaker",
//                            HttpMethod.POST, request, new ParameterizedTypeReference<String>() {
//                            });
//            String expected = "{\"patientName\":\"Shubham\"," +
//                    "\"reqStatus\":true," +
//                    "\"caretakerId\":\"string\"," +
//                    "\"patientId\":\"string\"," +
//                    "\"caretakerUsername\":\"Piyush\"," +
//                    "\"sentBy\":\"Shubham\"}\n";
//            System.out.println(response.getBody());
//            JSONAssert.assertEquals(expected, String.valueOf(response.getBody()), false);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void testIUpdateCaretakerStatus() {
//        UserCaretakerpojo user = new UserCaretakerpojo();
//        user.setCaretakerId("string");
//
//        HttpEntity<UserCaretakerpojo> entity = new HttpEntity<>(user);
//        try{
//            ResponseEntity<UserDetails> response = testRestTemplate.exchange(
//                    createURLWithPort("/api/caretaker/updatestatus")
//                    , HttpMethod.PUT, entity,UserDetails.class);
//
//            String expected = "{\n" +
//                    "  \"cId\": \"string\",\n" +
//                    "  \"caretakerId\": \"string\",\n" +
//                    "  \"caretakerUsername\": \"string\",\n" +
//                    "  \"createdAt\": \"string\",\n" +
//                    "  \"patientId\": \"string\",\n" +
//                    "  \"patientName\": \"string\",\n" +
//                    "  \"reqStatus\": true,\n" +
//                    "  \"sentBy\": \"string\"\n" +
//                    "}";
//            System.out.println(response.getBody());
//            JSONAssert.assertEquals(expected, String.valueOf(response.getBody()), false);
//        }
//        catch (Exception e){}
//    }
//
//    @Test
//    void testGetPatientsUnderMe() throws JSONException {
//        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//
//        ResponseEntity<String> response = testRestTemplate.exchange(
//                createURLWithPort("/api/caretaker/myPatients(Caretaker)?caretakerId=d0aba7ce-1e7f-458e-8090-8cc62c00c3c5")
//                , HttpMethod.GET, entity,String.class);
//
//        String expected
//                ="[{\"patientName\":\"Yatin\",\"reqStatus\":true,\"caretakerId\":\"d0aba7ce-1e7f-458e-8090-8cc62c00c3c5\",\"patientId\":\"5ecab2c4-eb9a-47c2-b23c-3b129d3bd9ca\",\"caretakerUsername\":\"Vinay\",\"createdAt\":\"2022-03-20\",\"sentBy\":\"P\",\"cid\":\"f757a544-1eb6-4765-b47b-8bb70c0ecb41\"}]\n";
//        System.out.println(response.getBody());
//        JSONAssert.assertEquals(expected,response.getBody(), false);
//    }
//
//    @Test
//    void testgetPatientRequests() throws JSONException {
//        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//
//        ResponseEntity<String> response = testRestTemplate.exchange(
//                createURLWithPort("/api/caretaker/patientRequests(Caretaker)?caretakerId=sdahggsjdsk")
//                , HttpMethod.GET, entity,String.class);
//
//        String expected
//                ="[{\"patientName\":\"Adiy\",\"reqStatus\":false,\"caretakerId\":\"sdahggsjdsk\",\"patientId\":\"GASKJHSj\",\"caretakerUsername\":\"Yas\",\"createdAt\":\"2022-03-24\",\"sentBy\":\"Adiy\",\"cid\":\"50fcefb4-a09c-4dba-b8fb-323cad335ed7\"}]\n";
//        System.out.println(response.getBody());
//        JSONAssert.assertEquals(expected,response.getBody(), false);
//    }
//
//    @Test
//    void testGetMyCaretakers() throws JSONException {
//        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//
//        ResponseEntity<String> response = testRestTemplate.exchange(
//                createURLWithPort("/api/caretaker/myCareTakers(Patient)?patientId=5ecab2c4-eb9a-47c2-b23c-3b129d3bd9ca")
//                , HttpMethod.GET, entity,String.class);
//
//        String expected
//                = "[{\"patientName\":\"Yatin\",\"reqStatus\":true,\"caretakerId\":\"379dfb3b-4b78-4232-84af-110d406622a6\",\"patientId\":\"5ecab2c4-eb9a-47c2-b23c-3b129d3bd9ca\",\"caretakerUsername\":\"Soni\",\"createdAt\":\"2022-03-26\",\"sentBy\":\"P\",\"cid\":\"680b6fb8-c674-4d92-989b-35dff3b9a1dc\"},{\"patientName\":\"Yatin\",\"reqStatus\":true,\"caretakerId\":\"d0aba7ce-1e7f-458e-8090-8cc62c00c3c5\",\"patientId\":\"5ecab2c4-eb9a-47c2-b23c-3b129d3bd9ca\",\"caretakerUsername\":\"Vinay\",\"createdAt\":\"2022-03-20\",\"sentBy\":\"P\",\"cid\":\"f757a544-1eb6-4765-b47b-8bb70c0ecb41\"}]\n";
//        System.out.println(response.getBody());
//        JSONAssert.assertEquals(expected,response.getBody(), false);
//    }
//
//
//    private String createURLWithPort(String uri) {
//        return "http://localhost:" + port + uri;
//    }
//
//}