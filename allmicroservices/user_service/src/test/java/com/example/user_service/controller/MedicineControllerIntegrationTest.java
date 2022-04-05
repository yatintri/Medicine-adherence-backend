package com.example.user_service.controller;

import com.example.user_service.pojos.Medicinepojo;

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

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MedicineControllerIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    HttpHeaders headers = new HttpHeaders();
    @Test
    void testSyncData() {
        List<Medicinepojo> list = new ArrayList<>();
        Medicinepojo user = new Medicinepojo();
        user.setCurrentCount(2);
        user.setDays("Sun");
        user.setEndDate("12/03");
        user.setMedicineDes("tablet");
        user.setMedicineName("dolo");
        user.setStartDate("11/03");
        user.setTime("string");
        user.setTitle("daily");
        user.setStatus(0);
        user.setTotalMedReminders(1);
        user.setUserId(4);
        list.add(user);



        HttpEntity<List<Medicinepojo>> request = new HttpEntity<>(list);
        try {
            ResponseEntity<String> response = testRestTemplate
                    .exchange("http://localhost:" + port +
                                    "/api/usermedicine/syncmedicines?userId=fb2b6415-e6b1-454d-9053-e07c5076858e",
                            HttpMethod.POST, request, new ParameterizedTypeReference<String>() {
                            });
            String expected = "{\"status\":\"OK\"}";

            JSONAssert.assertEquals(expected, String.valueOf(response.getBody()), false);
        }
        catch (Exception e) {

        }
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}














