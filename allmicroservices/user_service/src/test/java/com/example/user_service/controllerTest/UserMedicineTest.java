package com.example.user_service.controllerTest;

import com.example.user_service.controller.MedicineController;
import com.example.user_service.model.User;
import com.example.user_service.pojos.request.MedicineHistoryDTO;
import com.example.user_service.pojos.request.MedicinePojoDTO;
import com.example.user_service.pojos.response.medicine.SyncResponse;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserMedicineService;
import com.example.user_service.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)

class UserMedicineTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper=new ObjectMapper();

    @InjectMocks
    private MedicineController medicineController;

    @Mock
    UserMedicineService userMedicineService;

    @Mock
    UserRepository userRepository;

    BindingResult result = mock(BindingResult.class);


    @BeforeEach
    public void setUp(){
        this.mockMvc= MockMvcBuilders.standaloneSetup(medicineController).build();
    }

    User userEntity = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,null);
    MedicinePojoDTO medicinePojoDTO = new MedicinePojoDTO(123,"Mon",1,null,"something",10,"PCM","something",null,0,"10:00 AM");
    List<MedicinePojoDTO> medicinePojoDTOList = new ArrayList<>();

    SyncResponse syncResponse = new SyncResponse(Constants.SUCCESS, Constants.SYNC);
//    SyncResponse syncResponse1 = new SyncResponse(Constants.VALIDATION, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());

    @Test
    @DisplayName("Sync Test")
    @ExtendWith(MockitoExtension.class)
    void syncData() throws Exception {

        when(result.hasErrors()).thenReturn(false);
        ResponseEntity<SyncResponse> mav = medicineController.syncData("73578dfd-e7c9-4381-a348-113e72d80fa2", medicinePojoDTOList);
        medicinePojoDTOList.add(medicinePojoDTO);
        when(userMedicineService.syncData("73578dfd-e7c9-4381-a348-113e72d80fa2", medicinePojoDTOList)).thenReturn(syncResponse);
        String jsonText= objectMapper.writeValueAsString(medicinePojoDTOList);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/medicines/sync?userId=73578dfd-e7c9-4381-a348-113e72d80fa2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonText))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Sync medicine Test")
    @ExtendWith(MockitoExtension.class)
    void syncMedicineHistory() throws Exception{
        String[] timing= new String[4];
        timing[0]="10:00 AM";
        timing[1]="12:00 PM";
        when(result.hasErrors()).thenReturn(false);
        MedicineHistoryDTO medicineHistoryDTO= new MedicineHistoryDTO(1234,new Date(), timing,timing);
        MedicineHistoryDTO medicineHistoryDTO1= new MedicineHistoryDTO(1235,new Date(),timing,timing);
        MedicineHistoryDTO medicineHistoryDTO2= new MedicineHistoryDTO(1236,new Date(),timing,timing);
        List<MedicineHistoryDTO> medicineHistoryDTOList= new ArrayList<>();
        medicineHistoryDTOList.add(medicineHistoryDTO);
        medicineHistoryDTOList.add(medicineHistoryDTO1);
        medicineHistoryDTOList.add(medicineHistoryDTO2);
        String jsonText= objectMapper.writeValueAsString(medicineHistoryDTOList);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/medicine-history/sync?medicineId=123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonText))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Medicine History Test")
    @ExtendWith(MockitoExtension.class)
    void getMedicineHistories() throws Exception{

        when(result.hasErrors()).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/medicine-histories?medicineId=123&page=0&limit=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Medicine images Test")
    @ExtendWith(MockitoExtension.class)
    void getMedicineImages() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/medicine-images?medicineId=123&page=0&limit=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}

