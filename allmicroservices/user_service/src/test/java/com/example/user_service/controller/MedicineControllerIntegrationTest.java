package com.example.user_service.controller;

import com.example.user_service.pojos.dto.MedicineHistoryDTO;
import com.example.user_service.pojos.dto.MedicinePojo;

import org.json.JSONException;

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
import java.util.Date;
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
        List<MedicinePojo> list = new ArrayList<>();
        MedicinePojo user = new MedicinePojo();
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



        HttpEntity<List<MedicinePojo>> request = new HttpEntity<>(list);
        try {
            ResponseEntity<String> response = testRestTemplate
                    .exchange("http://localhost:" + port +
                                    "/api/v1/medicines/sync?userId=fb2b6415-e6b1-454d-9053-e07c5076858e",
                            HttpMethod.POST, request, new ParameterizedTypeReference<String>() {
                            });
            String expected = "{\"status\":\"OK\"}";

            JSONAssert.assertEquals(expected, String.valueOf(response.getBody()), false);
        }
        catch (Exception e) {

        }
    }
    @Test
    void testSyncMedicineHistory()
    {
        List<MedicineHistoryDTO> list = new ArrayList<>();
        MedicineHistoryDTO user = new MedicineHistoryDTO();
        user.setDate(String.valueOf(new Date("23/4/2022")));
        user.setRemId(23553637);
        user.setTaken(new String[]{"23:11"});
        user.setNot_taken(new String[]{"11:05"});

        HttpEntity<List<MedicineHistoryDTO>> request = new HttpEntity<>(list);
        try {
            ResponseEntity<String> response = testRestTemplate
                    .exchange("http://localhost:" + port +
                                    "/api/v1/medicine-history/sync?medId=12345",
                            HttpMethod.POST, request, new ParameterizedTypeReference<String>() {
                            });
            String expected = "OK";

            JSONAssert.assertEquals(expected, String.valueOf(response.getBody()), false);
        }
        catch (Exception e) {

        }
    }
    @Test
    void testGetMedicineHistory() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        try {
            ResponseEntity<String> response = testRestTemplate.exchange(
                    createURLWithPort("/api/v1/medicine-histories?medId=12345")
                    , HttpMethod.GET, entity, String.class);

            String expected
                    = "{\"status\":\"OK\",\"message\":\"Medicine History\"," +
                    "\"userMedicinesList\":[{\"historyId\":123467,\"date\":\"2019-04-28 20:15:15\"," +
                    "\"taken\":\"10:00,1:00,6:00\",\"notTaken\":\"11:05\"}]}\n";
            System.out.println(response.getBody());
            JSONAssert.assertEquals(expected, response.getBody(), false);
        }
        catch (Exception e){

        }
    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}














