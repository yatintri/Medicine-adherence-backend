//package com.example.user_service.controller;
//
//import com.example.user_service.model.UserDetails;
//import com.example.user_service.pojos.dto.UserCaretakerDTO;
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
//        UserCaretakerDTO user = new UserCaretakerDTO();
//
//        user.setCaretakerId("379dfb3b-4b78-4232-84af-110d406622a6");
//        user.setPatientId("23e3b129-2a1f-4306-9ee5-808357a3f4e6");
//        user.setPatientName("Pawan");
//        user.setReqStatus(true);
//        user.setCaretakerUsername("Rahul");
//        user.setSentBy("Rahul");
//
//
//        HttpEntity<UserCaretakerDTO> request = new HttpEntity<>(user);
//        try {
//            ResponseEntity<String> response = testRestTemplate
//                    .exchange("http://localhost:" + port +
//                                    "/api/v1/request",
//                            HttpMethod.POST, request, new ParameterizedTypeReference<String>() {
//                            });
//            String expected = "{\"status\":\"failed\"," +
//                    "\"message\":\"Caretaker Already Present!!\"," +
//                    "\"userCaretaker\":null}\n";
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
//        UserCaretakerDTO user = new UserCaretakerDTO();
//        user.setCaretakerId("string");
//
//        HttpEntity<UserCaretakerDTO> entity = new HttpEntity<>(user);
//        try{
//            ResponseEntity<UserDetails> response = testRestTemplate.exchange(
//                    createURLWithPort("/api/v1/accept?cId=09c27d37-5c41-46fc-b3a0-7d48142fa86e")
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
//                createURLWithPort("/api/v1/patients?caretakerId=d0aba7ce-1e7f-458e-8090-8cc62c00c3c5")
//                , HttpMethod.GET, entity,String.class);
//
//        String expected =
//                "{\"status\":\"Success\",\"message\":\"Data found\",\"userCaretakerList\":[{\"patientName\":\"Vardhini\"," +
//                        "\"reqStatus\":true,\"caretakerId\":\"d0aba7ce-1e7f-458e-8090-8cc62c00c3c5\"," +
//                        "\"patientId\":\"dc7549b2-77a1-4ea5-9e4a-9c690ff77577\",\"caretakerUsername\":\"Vinay\"," +
//                        "\"createdAt\":\"2022-05-24\",\"sentBy\":\"P\"," +
//                        "\"cid\":\"05315248-bfbe-4376-ba1e-05ce23961e72\"}," +
//                        "{\"patientName\":\"Abhijeet\",\"reqStatus\":true," +
//                        "\"caretakerId\":\"d0aba7ce-1e7f-458e-8090-8cc62c00c3c5\"," +
//                        "\"patientId\":\"812c6c43-d6d3-4024-a236-bc4deb730375\",\"caretakerUsername\":\"Vinay\",\"createdAt\":\"2022-05-25\",\"sentBy\":\"P\",\"cid\":\"11edda3c-d358-4dee-8a2a-47be9a932e2e\"},{\"patientName\":\"ABHIJEET\",\"reqStatus\":true,\"caretakerId\":\"d0aba7ce-1e7f-458e-8090-8cc62c00c3c5\"," +
//                        "\"patientId\":\"74e5ccb2-4e9c-4be5-bd0e-fd64a65690bd\",\"caretakerUsername\":\"Vinay\",\"createdAt\":\"2022-05-23\",\"sentBy\":\"P\",\"cid\":\"38f1f2f3-9c20-4759-8a1c-b2e8048c2520\"},{\"patientName\":\"Nav\",\"reqStatus\":true,\"caretakerId\":\"d0aba7ce-1e7f-458e-8090-8cc62c00c3c5\"," +
//                        "\"patientId\":\"17876ef9-38c0-4c81-86bf-5b7710d375b7\",\"caretakerUsername\":\"Vinay\",\"createdAt\":\"2022-05-25\",\"sentBy\":\"P\",\"cid\":\"50adc2d3-6401-4bb8-962a-14549fb8c0be\"},{\"patientName\":\"Muskan\",\"reqStatus\":true,\"caretakerId\":\"d0aba7ce-1e7f-458e-8090-8cc62c00c3c5\",\"patientId\":\"b4a838ac-c79a-4043-88d7-4c9b3395c7c1\",\"caretakerUsername\":\"Vinay\",\"createdAt\":\"2022-05-23\",\"sentBy\":\"P\",\"cid\":\"d4972768-3534-4002-a2d7-5d29968f0e18\"},{\"patientName\":\"Muskan\",\"reqStatus\":true,\"caretakerId\":\"d0aba7ce-1e7f-458e-8090-8cc62c00c3c5\",\"patientId\":\"a36f6565-2272-4761-8092-9ecc02f4501c\",\"caretakerUsername\":\"Vinay\",\"createdAt\":\"2022-05-10\",\"sentBy\":\"P\",\"cid\":\"d84e069b-bd32-4751-9887-6c2e2df5ac39\"},{\"patientName\":\"Vinay Kumar\",\"reqStatus\":true,\"caretakerId\":\"d0aba7ce-1e7f-458e-8090-8cc62c00c3c5\",\"patientId\":\"1e05fe7b-bb6d-40df-8930-c06abe308782\",\"caretakerUsername\":\"Vinay\",\"createdAt\":\"2022-05-23\",\"sentBy\":\"P\",\"cid\":\"dc363f4c-46da-488b-9b05-9ff50e042287\"},{\"patientName\":\"Yatin\",\"reqStatus\":true,\"caretakerId\":\"d0aba7ce-1e7f-458e-8090-8cc62c00c3c5\",\"patientId\":\"5ecab2c4-eb9a-47c2-b23c-3b129d3bd9ca\",\"caretakerUsername\":\"Vinay\",\"createdAt\":\"2022-03-20\",\"sentBy\":\"P\",\"cid\":\"f757a544-1eb6-4765-b47b-8bb70c0ecb41\"}]}\n";
//        System.out.println(response.getBody());
//        JSONAssert.assertEquals(expected,response.getBody(), false);
//    }
//
//    @Test
//    void testGetPatientRequests() throws JSONException {
//        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//
//        ResponseEntity<String> response = testRestTemplate.exchange(
//                createURLWithPort("/api/v1/patient/requests?caretakerId=sdahggsjdsk")
//                , HttpMethod.GET, entity,String.class);
//
//        String expected =
//                "{\"status\":\"Success\",\"message\":\"Data found\",\"userCaretakerList\":[{\"patientName\":\"Adiy\",\"reqStatus\":false,\"caretakerId\":\"sdahggsjdsk\",\"patientId\":\"GASKJHSj\",\"caretakerUsername\":\"Yas\",\"createdAt\":\"2022-03-24\",\"sentBy\":\"Adiy\",\"cid\":\"50fcefb4-a09c-4dba-b8fb-323cad335ed7\"}]}\n";
//        System.out.println(response.getBody());
//        JSONAssert.assertEquals(expected,response.getBody(), false);
//    }
//
//    @Test
//    void testGetMyCaretakers() throws JSONException {
//        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//
//        ResponseEntity<String> response = testRestTemplate.exchange(
//                createURLWithPort("/api/v1/caretakers?patientId=5ecab2c4-eb9a-47c2-b23c-3b129d3bd9ca")
//                , HttpMethod.GET, entity,String.class);
//
//        String expected =
//                "{\"status\":\"Success\",\"message\":\"Data found\"," +
//                        "\"userCaretakerList\":[{\"patientName\":\"Yatin\"," +
//                        "\"reqStatus\":true,\"caretakerId\":\"379dfb3b-4b78-4232-84af-110d406622a6\"," +
//                        "\"patientId\":\"5ecab2c4-eb9a-47c2-b23c-3b129d3bd9ca\"," +
//                        "\"caretakerUsername\":\"Soni\",\"createdAt\":\"2022-03-26\",\"sentBy\":\"P\",\"cid\":\"680b6fb8-c674-4d92-989b-35dff3b9a1dc\"},{\"patientName\":\"Yatin\",\"reqStatus\":true,\"caretakerId\":\"d0aba7ce-1e7f-458e-8090-8cc62c00c3c5\",\"patientId\":\"5ecab2c4-eb9a-47c2-b23c-3b129d3bd9ca\",\"caretakerUsername\":\"Vinay\",\"createdAt\":\"2022-03-20\"," +
//                        "\"sentBy\":\"P\",\"cid\":\"f757a544-1eb6-4765-b47b-8bb70c0ecb41\"}]}\n";
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