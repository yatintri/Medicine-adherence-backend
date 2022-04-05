//package com.example.user_service.controller;
//
//import com.example.user_service.model.UserMedReminder;
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
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class UserMedRemControllerTest {
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//
//    @LocalServerPort
//    private int port;
//
//    HttpHeaders headers = new HttpHeaders();
//    @Test
//    void saveMedReminders() throws JSONException {
//        UserMedReminder user = new UserMedReminder();
//        user.setReminder_id("d9f15ed2-8508-4366-b721-fc65d2d979e2");
//        user.setReminder_title("string");
//        user.setCreated_at("string");
//        user.setStart_date("string");user.setEnd_date("string");
//        user.setEveryday(true);
//        user.setReminder_status(true);user.setDays("string");
//        user.setReminder_time("string");
//
//
//
//        HttpEntity<UserMedReminder> request = new HttpEntity<>(user);
//
//            ResponseEntity<String> response = testRestTemplate
//                    .exchange("http://localhost:" + port +
//                                    "/api/user/medreminder/saveMedReminder?medicine_id=34a9364d-2f56-43de-84c1-855727499137",
//                            HttpMethod.POST, request, new ParameterizedTypeReference<String>() {
//                            });
//            String expected = "{\n" +
//                    "  \"reminder_id\": \"d9f15ed2-8508-4366-b721-fc65d2d979e2\",\n" +
//                    "  \"reminder_title\": \"string\",\n" +
//                    "  \"created_at\": \"string\",\n" +
//                    "  \"start_date\": \"string\",\n" +
//                    "  \"end_date\": \"string\",\n" +
//                    "  \"everyday\": true,\n" +
//                    "  \"reminder_status\": true,\n" +
//                    "  \"days\": \"string\",\n" +
//                    "  \"reminder_time\": \"string\"\n" +
//                    "}";
//            System.out.println(response.getBody());
//            JSONAssert.assertEquals(expected, String.valueOf(response.getBody()), false);
//        }
//        @Test
//        void testGetMedRemById() throws JSONException {
//            HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//
//            ResponseEntity<String> response = testRestTemplate.exchange(
//                    createURLWithPort("/api/user/medreminder/getUserMedRem?medicine_id=34a9364d-2f56-43de-84c1-855727499137")
//                    , HttpMethod.GET, entity,String.class);
//
//            String expected =
//                    "{\n" +
//                            "  \"medicine_id\": \"34a9364d-2f56-43de-84c1-855727499137\",\n" +
//                            "  \"create_time\": \"string\",\n" +
//                            "  \"medicine_name\": \"asprin\",\n" +
//                            "  \"medicine_des\": \"headache\",\n" +
//                            "  \"active_status\": true,\n" +
//                            "  \"reminderEntity\": {\n" +
//                            "    \"reminder_id\": \"d9f15ed2-8508-4366-b721-fc65d2d979e2\",\n" +
//                            "    \"reminder_title\": \"string\",\n" +
//                            "    \"created_at\": \"string\",\n" +
//                            "    \"start_date\": \"string\",\n" +
//                            "    \"end_date\": \"string\",\n" +
//                            "    \"everyday\": true,\n" +
//                            "    \"reminder_status\": true,\n" +
//                            "    \"days\": \"string\",\n" +
//                            "    \"reminder_time\": \"string\"\n" +
//                            "  }\n" +
//                            "}";
//            System.out.println(response.getBody());
//            JSONAssert.assertEquals(expected,response.getBody(), false);
//        }
//        private String createURLWithPort(String uri) {
//            return "http://localhost:" + port + uri;
//        }
//
//    }
