package com.example.user_service.controllerTest;

import com.example.user_service.controller.CaretakerController;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.pojos.dto.SendImageDto;
import com.example.user_service.pojos.dto.UserCaretakerDTO;
import com.example.user_service.pojos.response.caretaker.CaretakerResponse;
import com.example.user_service.pojos.response.caretaker.CaretakerResponsePage;
import com.example.user_service.service.caretaker.CareTakerService;
import com.example.user_service.util.Messages;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.example.user_service.pojos.response.image.ImageResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
class UserCaretakerTest {
 private MockMvc mockMvc;

 private final ObjectMapper objectMapper=new ObjectMapper();

 @InjectMocks
 CaretakerController caretakerController;

 @Mock
 CareTakerService careTakerService;

 @Mock
 RabbitTemplate rabbitTemplate= new RabbitTemplate();

 @BeforeEach
 public void setUp(){
  this.mockMvc= MockMvcBuilders.standaloneSetup(caretakerController).build();
 }


 UserCaretakerDTO userCaretakerDTO= new UserCaretakerDTO("vinay",false,"73578dfd-e7c9-4381-a348-113e72d80fa2","548259761235609","nikunj",null ,"p");
 UserCaretaker userCaretaker= new UserCaretaker();
 List<UserCaretaker> userCaretakerList= new ArrayList<>();
 ImageResponse imageResponse= new ImageResponse();
 CaretakerResponse caretakerResponse = new CaretakerResponse();
 CaretakerResponsePage caretakerResponsePage = new CaretakerResponsePage();

 @Test
@DisplayName("Save caretaker Test")
 @ExtendWith(MockitoExtension.class)
 void saveCaretaker() throws Exception, UserExceptions {
  UserCaretakerDTO userCaretakerDTO1= new UserCaretakerDTO("vinay",false,"73578dfd-e7c9-4381-a348-113e72d80fa2","548259761235609","nikunj",LocalDateTime.now()
          ,"p");
  objectMapper.registerModule(new JavaTimeModule());

  objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,false);
  Mockito.when(careTakerService.saveCareTaker(userCaretakerDTO1)).thenReturn(caretakerResponse);
  String jsonText = objectMapper.writeValueAsString(userCaretakerDTO1);
  mockMvc.perform(MockMvcRequestBuilders
                  .post("/api/v1/request")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(jsonText))
          .andExpect(MockMvcResultMatchers.status().isOk());
 }

 @Test
 @DisplayName("Update caretaker Test")
 @ExtendWith(MockitoExtension.class)
 void updateCaretakerStatus() throws Exception, UserExceptions {
  Mockito.when(careTakerService.updateCaretakerStatus("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(caretakerResponse);
  mockMvc.perform(MockMvcRequestBuilders
                  .put("/api/v1/accept?cId=73578dfd-e7c9-4381-a348-113e72d80fa2")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk());
 }

 @Test
 @DisplayName("Get Patients under caretaker Test")
 @ExtendWith(MockitoExtension.class)
 void getPatientsUnderMe() throws Exception{
  mockMvc.perform(MockMvcRequestBuilders
                  .get("/api/v1/patients?caretakerId=73578dfd-e7c9-4381-a348-113e72d80fa2")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk());

 }
 @Test
 @DisplayName("Get Patients Request for caretaker Test")
 @ExtendWith(MockitoExtension.class)
 void getPatientRequestsC() throws Exception, UserExceptions {
  Mockito.when(careTakerService.getPatientRequests("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2)).thenReturn(caretakerResponsePage);
  mockMvc.perform(MockMvcRequestBuilders
                  .get("/api/v1/patient/requests?caretakerId=73578dfd-e7c9-4381-a348-113e72d80fa2&page=0&limit=2")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk());
 }

 @Test
 @DisplayName("Get my caretaker Test")
 @ExtendWith(MockitoExtension.class)
 void getMyCaretakers() throws Exception, UserExceptions {
  Mockito.when(careTakerService.getMyCaretakers("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2)).thenReturn(caretakerResponsePage);
  mockMvc.perform(MockMvcRequestBuilders
                  .get("/api/v1/caretakers?patientId=73578dfd-e7c9-4381-a348-113e72d80fa2&page=0&limit=2")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk());
 }

 @Test
 @DisplayName("Get caretaker request Test")
 @ExtendWith(MockitoExtension.class)
 void getCaretakerRequestsP() throws Exception, UserExceptions {
  Mockito.when(careTakerService.getCaretakerRequestsP("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2)).thenReturn(caretakerResponsePage);
  mockMvc.perform(MockMvcRequestBuilders
                  .get("/api/v1/caretaker/requests?patientId=73578dfd-e7c9-4381-a348-113e72d80fa2&page=0&limit=2")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk());
 }

 @Test
 @DisplayName("Delete Patient request Test")
 @ExtendWith(MockitoExtension.class)
 void delPatientReq() throws Exception, UserExceptions {
  Mockito.when(careTakerService.delPatientReq("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(Messages.SUCCESS);
  mockMvc.perform(MockMvcRequestBuilders
                  .get("/api/v1/delete?cId=73578dfd-e7c9-4381-a348-113e72d80fa2")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk());
 }


 @Test
 @DisplayName("Notification Test")
 @ExtendWith(MockitoExtension.class)
 void notifyUserForMed() throws Exception {
  mockMvc.perform(MockMvcRequestBuilders
                  .get("/api/v1/notifyuser?fcmToken=egufagfljbgalgfoeiugi&medname=PCM")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk());
 }

 @Test
 @DisplayName("Send Image to caretaker Test")
 @ExtendWith(MockitoExtension.class)
 void sendImageToCaretaker()throws Exception {
  MockMultipartFile employeeJson = new MockMultipartFile("employee", null,
          "application/json", "{\"name\": \"Emp Name\"}".getBytes());
  SendImageDto sendImageDto= new SendImageDto(employeeJson,"Vinay","PCM","73578dfd-e7c9-4381-a348-113e72d80fa2",123);
  mockMvc.perform(MockMvcRequestBuilders
                  .multipart("/api/v1/image")
                  .file("image",sendImageDto.getImage().getBytes())
                  .param("name",sendImageDto.getName())
                  .param("medName",sendImageDto.getMedName())
                  .param("id",sendImageDto.getId())
                  .param("medId",sendImageDto.getMedId().toString())
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk());
 }

}
