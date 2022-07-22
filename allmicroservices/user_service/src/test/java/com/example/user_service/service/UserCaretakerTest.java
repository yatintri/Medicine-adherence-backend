package com.example.user_service.service;

import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.model.MedicineHistory;
import com.example.user_service.model.User;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.request.SendImageDto;
import com.example.user_service.pojos.request.UserCaretakerDTO;
import com.example.user_service.pojos.response.caretaker.CaretakerResponse;
import com.example.user_service.pojos.response.caretaker.CaretakerResponse1;
import com.example.user_service.pojos.response.caretaker.CaretakerResponsePage;
import com.example.user_service.pojos.response.image.SendImageResponse;
import com.example.user_service.repository.ImageRepository;
import com.example.user_service.repository.UserCaretakerRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.service.impl.CareTakerServiceImpl;
import com.example.user_service.util.Constants;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
 class UserCaretakerTest {

    CareTakerServiceImpl careTakerServiceImpl;

    @Mock
    UserCaretakerRepository userCaretakerRepository;

    @Mock
    ModelMapper mapper;

    @Mock
    ImageRepository imageRepository;

    @Mock
    UserMedicineRepository userMedicineRepository;
    @Mock
    RabbitTemplate rabbitTemplate;

    @BeforeEach
    void init(){
        careTakerServiceImpl = new CareTakerServiceImpl(imageRepository,userMedicineRepository,rabbitTemplate,userCaretakerRepository,mapper);
    }

    @Test
    @DisplayName("Save caretaker exception Test")
    void saveCaretakerException(){
        UserCaretakerDTO userCaretakerDTO= new UserCaretakerDTO("Yatin",true,"gdhdl","gsgkj","Nikunj",LocalDateTime.now(),"jha");
        UserCaretaker userCaretaker= new UserCaretaker();
        when(mapper.map(userCaretakerDTO,UserCaretaker.class)).thenReturn(userCaretaker);
        when(userCaretakerRepository.check(userCaretaker.getPatientId(),userCaretaker.getCaretakerId())).thenReturn(userCaretaker);
        try{
            careTakerServiceImpl.saveCareTaker(userCaretakerDTO);
        }catch (UserCaretakerException | UserExceptionMessage userCaretakerException){
            Assertions.assertEquals(Constants.ALREADY_PRESENT,userCaretakerException.getMessage());
        }
    }
    @Test
    @DisplayName("Save caretaker sql Test")
    void saveCaretakerSqlException(){
        UserCaretakerDTO userCaretakerDTO= new UserCaretakerDTO("Yatin",true,"gdhdl","gsgkj","Nikunj",LocalDateTime.now(),"jha");
        UserCaretaker userCaretaker= new UserCaretaker();
        when(mapper.map(userCaretakerDTO,UserCaretaker.class)).thenReturn(userCaretaker);
        when(userCaretakerRepository.check(userCaretaker.getPatientId(),userCaretaker.getCaretakerId())).thenThrow(JDBCConnectionException.class);
        try{
            careTakerServiceImpl.saveCareTaker(userCaretakerDTO);
        }catch (UserCaretakerException | UserExceptionMessage userCaretakerException){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userCaretakerException.getMessage());
        }
    }
    @Test
    @DisplayName("Save caretaker Test")
    void saveCaretakerTest() throws UserCaretakerException, UserExceptionMessage {
        UserCaretakerDTO userCaretakerDTO= new UserCaretakerDTO();
        userCaretakerDTO.setPatientName("vinay");
        UserCaretaker userCaretaker= new UserCaretaker("jadkhk","Yatin",true,"gdhdl","gsgkj","Nikunj", LocalDateTime.now(),LocalDateTime.now(),"jha",true);
        when(mapper.map(userCaretakerDTO,UserCaretaker.class)).thenReturn(userCaretaker);
        when(userCaretakerRepository.check(userCaretaker.getPatientId(),userCaretaker.getCaretakerId())).thenReturn(null);
        CaretakerResponse userCaretaker1= careTakerServiceImpl.saveCareTaker(userCaretakerDTO);
        Assertions.assertEquals(userCaretaker.getPatientName(),userCaretaker1.getUserCaretaker().getPatientName());
    }

    @Test
    @DisplayName("Update caretaker exception Test")
    void updateCaretakerStatusException(){
        UserCaretaker userCaretaker= new UserCaretaker();
        when(userCaretakerRepository.findById("feyfacakuvejgclgaglgu")).thenReturn(Optional.of(userCaretaker));
        try{
            careTakerServiceImpl.updateCaretakerStatus("feyfacakuvejgclgaglgu");
        }catch (UserCaretakerException  userCaretakerException){
            Assertions.assertEquals(Constants.MSG,userCaretakerException.getMessage());
        }
    }
    @Test
    @DisplayName("Update caretaker sql exception Test")
    void updateCaretakerStatusSqlException(){
        UserCaretaker userCaretaker= new UserCaretaker();
        when(userCaretakerRepository.findById("feyfacakuvejgclgaglgu")).thenThrow(JDBCConnectionException.class);
        try{
            careTakerServiceImpl.updateCaretakerStatus("feyfacakuvejgclgaglgu");
        }catch (UserCaretakerException  userCaretakerException){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userCaretakerException.getMessage());
        }
    }

    @Test
    @DisplayName("Update caretaker Test")
    void updateCaretakerStatusTest() throws UserCaretakerException {
        UserCaretaker userCaretaker= new UserCaretaker("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay",true,"ftdutsaduifioadf","yfuydfafakjfdafdou","nikunj",LocalDateTime.now(),LocalDateTime.now(),"p",false);
        when(userCaretakerRepository.findById(userCaretaker.getId())).thenReturn(Optional.of(userCaretaker));
        CaretakerResponse userCaretaker1= careTakerServiceImpl.updateCaretakerStatus("73578dfd-e7c9-4381-a348-113e72d80fa2");
        Assertions.assertEquals(userCaretaker.getCaretakerId(),userCaretaker1.getUserCaretaker().getCaretakerId());
    }

    @Test
    @DisplayName("Get patients under me exception Test")
    void getPatientsUnderMeException(){
        int pageNo=0;
        int pageSize = 2;
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        UserCaretaker userCaretaker= new UserCaretaker();
        List<UserCaretaker> userCaretakerList= new ArrayList<>();
        Page<UserCaretaker> userCaretakerPage= new PageImpl<>(userCaretakerList);
        when(userCaretakerRepository.getPatientsUnderMe("73578dfd-e7c9-4381-a348-113e72d80fa2",pageable)).thenReturn(userCaretakerPage);
        try{
            careTakerServiceImpl.getPatientsUnderMe("73578dfd-e7c9-4381-a348-113e72d80fa2",pageNo,pageSize);
        }catch (UserCaretakerException userCaretakerException){
            Assertions.assertEquals(Constants.MSG,userCaretakerException.getMessage());
        }
    }
    @Test
    @DisplayName("Get patients under me  sql exception Test")
    void getPatientsUnderMeSqlException(){
        int pageNo=0;
        int pageSize = 2;
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        UserCaretaker userCaretaker= new UserCaretaker();
        List<UserCaretaker> userCaretakerList= new ArrayList<>();
        Page<UserCaretaker> userCaretakerPage= new PageImpl<>(userCaretakerList);
        when(userCaretakerRepository.getPatientsUnderMe("73578dfd-e7c9-4381-a348-113e72d80fa2",pageable)).thenThrow(JDBCConnectionException.class);
        try{
            careTakerServiceImpl.getPatientsUnderMe("73578dfd-e7c9-4381-a348-113e72d80fa2",pageNo,pageSize);
        }catch (UserCaretakerException userCaretakerException){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userCaretakerException.getMessage());
        }
    }

    @Test
    @DisplayName("Get patients under me Test")
    void getPatientsUnderMeTest() throws UserCaretakerException {
        int pageNo=0;
        int pageSize = 2;
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        UserCaretaker userCaretaker= new UserCaretaker();
        List<UserCaretaker> userCaretakerList= new ArrayList<>();
        userCaretakerList.add(userCaretaker);
        Page<UserCaretaker> userCaretakerPage= new PageImpl<>(userCaretakerList);
        CaretakerResponsePage caretakerResponsePage= new CaretakerResponsePage(Constants.SUCCESS, Constants.DATA_FOUND,5L,2,0,userCaretakerList);
        when(userCaretakerRepository.getPatientsUnderMe("73578dfd-e7c9-4381-a348-113e72d80fa2",pageable)).thenReturn(userCaretakerPage);
        CaretakerResponsePage caretakerResponsePage1= careTakerServiceImpl.getPatientsUnderMe("73578dfd-e7c9-4381-a348-113e72d80fa2",pageNo,pageSize);
        Assertions.assertEquals(caretakerResponsePage.getUserCaretakerStream(),caretakerResponsePage1.getUserCaretakerStream());
    }

    @Test
    @DisplayName("Get patients request exception Test")
    void getPatientRequestException(){
        int pageNo=0;
        int pageSize = 2;
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        List<UserCaretaker> userCaretakerList= new ArrayList<>();
        Page<UserCaretaker> userCaretakerPage= new PageImpl<>(userCaretakerList);
        when(userCaretakerRepository.getPatientRequests("73578dfd-e7c9-4381-a348-113e72d80fa2",pageable)).thenReturn(userCaretakerPage);
        try{
            careTakerServiceImpl.getPatientRequests("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2);
        }catch (UserCaretakerException userCaretakerException){
            Assertions.assertEquals(Constants.MSG,userCaretakerException.getMessage());
        }
    }
    @Test
    @DisplayName("Get patients request sql exception Test")
    void getPatientRequestSqlException(){
        int pageNo=0;
        int pageSize = 2;
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        List<UserCaretaker> userCaretakerList= new ArrayList<>();
        Page<UserCaretaker> userCaretakerPage= new PageImpl<>(userCaretakerList);
        when(userCaretakerRepository.getPatientRequests("73578dfd-e7c9-4381-a348-113e72d80fa2",pageable)).thenThrow(JDBCConnectionException.class);
        try{
            careTakerServiceImpl.getPatientRequests("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2);
        }catch (UserCaretakerException userCaretakerException){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userCaretakerException.getMessage());
        }
    }

    @Test
    @DisplayName("Get patients request Test")
    void getPatientRequestTest() throws UserCaretakerException {
        int pageNo=0;
        int pageSize = 2;
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        UserCaretaker userCaretaker = new UserCaretaker();
        List<UserCaretaker> userCaretakerList = new ArrayList<>();
        userCaretakerList.add(userCaretaker);
        Page<UserCaretaker> userCaretakerPage= new PageImpl<>(userCaretakerList);
        CaretakerResponsePage caretakerResponsePage= new CaretakerResponsePage(Constants.SUCCESS, Constants.DATA_FOUND,5L,2,0,userCaretakerList);
        when(userCaretakerRepository.getPatientRequests("73578dfd-e7c9-4381-a348-113e72d80fa2",pageable)).thenReturn(userCaretakerPage);
        CaretakerResponsePage userCaretakerList1 = careTakerServiceImpl.getPatientRequests("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2);
        Assertions.assertEquals(caretakerResponsePage.getUserCaretakerStream(),userCaretakerList1.getUserCaretakerStream());
    }

    @Test
    @DisplayName("Get my caretaker exception Test")
    void getMyCaretakersException(){
        int pageNo=0;
        int pageSize = 2;
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        List<UserCaretaker> userCaretakerList= new ArrayList<>();
        Page<UserCaretaker> userCaretakerPage= new PageImpl<>(userCaretakerList);
        when(userCaretakerRepository.getMyCaretakers("73578dfd-e7c9-4381-a348-113e72d80fa2",pageable)).thenReturn(userCaretakerPage);
        try{
            careTakerServiceImpl.getMyCaretakers("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2);
        }catch (UserCaretakerException userCaretakerException){
            Assertions.assertEquals(Constants.MSG,userCaretakerException.getMessage());
        }
    }

    @Test
    @DisplayName("Get my caretaker sql exception Test")
    void getMyCaretakersSqlException(){
        int pageNo=0;
        int pageSize = 2;
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        List<UserCaretaker> userCaretakerList= new ArrayList<>();
        Page<UserCaretaker> userCaretakerPage= new PageImpl<>(userCaretakerList);
        when(userCaretakerRepository.getMyCaretakers("73578dfd-e7c9-4381-a348-113e72d80fa2",pageable)).thenThrow(JDBCConnectionException.class);
        try{
            careTakerServiceImpl.getMyCaretakers("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2);
        }catch (UserCaretakerException userCaretakerException){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userCaretakerException.getMessage());
        }
    }
    @Test
    @DisplayName("Get my caretaker Test")
    void getMyCaretakersTest() throws UserCaretakerException {
        int pageNo=0;
        int pageSize = 2;
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        UserCaretaker userCaretaker = new UserCaretaker();
        List<UserCaretaker> userCaretakerList = new ArrayList<>();
        userCaretakerList.add(userCaretaker);
        Page<UserCaretaker> userCaretakerPage= new PageImpl<>(userCaretakerList);
        CaretakerResponsePage caretakerResponsePage= new CaretakerResponsePage(Constants.SUCCESS, Constants.DATA_FOUND,5L,2,0,userCaretakerList);
        when(userCaretakerRepository.getMyCaretakers("73578dfd-e7c9-4381-a348-113e72d80fa2",pageable)).thenReturn(userCaretakerPage);
        CaretakerResponsePage userCaretakerList1 = careTakerServiceImpl.getMyCaretakers("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2);
        Assertions.assertEquals(caretakerResponsePage.getUserCaretakerStream(),userCaretakerList1.getUserCaretakerStream());
    }

    @Test
    @DisplayName("Get my caretaker status exception Test")
    void getCaretakerRequestStatusException(){
        List<UserCaretaker> userCaretakerList= new ArrayList<>();
        when(userCaretakerRepository.getCaretakerRequestStatus("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(userCaretakerList);
        try{
            careTakerServiceImpl.getCaretakerRequestStatus("73578dfd-e7c9-4381-a348-113e72d80fa2");
        }catch (UserCaretakerException userCaretakerException){
            Assertions.assertEquals(Constants.NO_RECORD_FOUND,userCaretakerException.getMessage());
        }
    }

    @Test
    @DisplayName("Get my caretaker status sql exception Test")
    void getCaretakerRequestStatusSqlException(){
        List<UserCaretaker> userCaretakerList= new ArrayList<>();
        when(userCaretakerRepository.getCaretakerRequestStatus("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenThrow(JDBCConnectionException.class);
        try{
            careTakerServiceImpl.getCaretakerRequestStatus("73578dfd-e7c9-4381-a348-113e72d80fa2");
        }catch (UserCaretakerException userCaretakerException){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userCaretakerException.getMessage());
        }
    }

    @Test
    @DisplayName("Get my caretaker status  Test")
    void getCaretakerRequestStatusTest() throws UserCaretakerException {
        UserCaretaker userCaretaker = new UserCaretaker();
        List<UserCaretaker> userCaretakerList = new ArrayList<>();
        userCaretakerList.add(userCaretaker);
        CaretakerResponse1 caretakerResponse = new CaretakerResponse1(Constants.SUCCESS, Constants.DATA_FOUND,userCaretakerList);
        when(userCaretakerRepository.getCaretakerRequestStatus("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(userCaretakerList);
        CaretakerResponse1 userCaretakerList1 = careTakerServiceImpl.getCaretakerRequestStatus("73578dfd-e7c9-4381-a348-113e72d80fa2");
        Assertions.assertEquals(caretakerResponse.getUserCaretakerList().size(),userCaretakerList1.getUserCaretakerList().size());
    }

    @Test
    @DisplayName("Get my caretaker status P exception Test")
    void getCaretakerRequestPException(){
        int pageNo=0;
        int pageSize = 2;
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        List<UserCaretaker> userCaretakerList= new ArrayList<>();
        Page<UserCaretaker> userCaretakerPage= new PageImpl<>(userCaretakerList);
        when(userCaretakerRepository.getCaretakerRequestsForPatients("73578dfd-e7c9-4381-a348-113e72d80fa2",pageable)).thenReturn(userCaretakerPage);
        try{
            careTakerServiceImpl.getCaretakerRequestsForPatient("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2);
        }catch (UserCaretakerException userCaretakerException){
            Assertions.assertEquals(Constants.MSG,userCaretakerException.getMessage());
        }
    }
    @Test
    @DisplayName("Get my caretaker status P Sqlexception Test")
    void getCaretakerRequestPSqlException(){
        int pageNo=0;
        int pageSize = 2;
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        List<UserCaretaker> userCaretakerList= new ArrayList<>();
        Page<UserCaretaker> userCaretakerPage= new PageImpl<>(userCaretakerList);
        when(userCaretakerRepository.getCaretakerRequestsForPatients("73578dfd-e7c9-4381-a348-113e72d80fa2",pageable)).thenThrow(JDBCConnectionException.class);
        try{
            careTakerServiceImpl.getCaretakerRequestsForPatient("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2);
        }catch (UserCaretakerException userCaretakerException){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userCaretakerException.getMessage());
        }
    }


    @Test
    @DisplayName("Get my caretaker status P Test")
    void getCaretakerRequestPTest() throws UserCaretakerException{
        int pageNo=0;
        int pageSize = 2;
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        UserCaretaker userCaretaker = new UserCaretaker();
        List<UserCaretaker> userCaretakerList = new ArrayList<>();
        userCaretakerList.add(userCaretaker);
        Page<UserCaretaker> userCaretakerPage= new PageImpl<>(userCaretakerList);
        CaretakerResponsePage caretakerResponsePage= new CaretakerResponsePage(Constants.SUCCESS, Constants.DATA_FOUND,5L,2,0,userCaretakerList);
        when(userCaretakerRepository.getCaretakerRequestsForPatients("73578dfd-e7c9-4381-a348-113e72d80fa2",pageable)).thenReturn(userCaretakerPage);
        CaretakerResponsePage userCaretakerList1 = careTakerServiceImpl.getCaretakerRequestsForPatient("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2);
        Assertions.assertEquals(caretakerResponsePage.getUserCaretakerStream(),userCaretakerList1.getUserCaretakerStream());
    }

    @Test
    @DisplayName("Delete patient request exception Test")
    void delPatientReqException() {
        Optional<UserCaretaker> userCaretaker= Optional.empty();
        when(userCaretakerRepository.findById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(userCaretaker);
        try{careTakerServiceImpl.deletePatientRequest("73578dfd-e7c9-4381-a348-113e72d80fa2");
        }catch (UserCaretakerException | UserExceptionMessage userCaretakerException){
            Assertions.assertEquals(Constants.MSG,userCaretakerException.getMessage());
        }
    }

//    @Test
//    @DisplayName("Delete patient request Test")
//    void delPatientReqTest() throws UserExceptionMessage, UserCaretakerException {
//        UserCaretaker userCaretaker= new UserCaretaker("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay",true,"ftdutsaduifioadf","yfuydfafakjfdafdou","nikunj",LocalDateTime.now(),LocalDateTime.now(),"p");
//        Optional<UserCaretaker> userCaretakerTest= Optional.of(userCaretaker);
//        when(userCaretakerRepository.findById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(userCaretakerTest);
//        String text =careTakerServiceImpl.deletePatientRequest("73578dfd-e7c9-4381-a348-113e72d80fa2");
//        Assertions.assertEquals(Constants.SUCCESS,text);
//    }

    @Test
    @DisplayName("Send image exception Test")
    void sendImageToCaretakerException() throws UserCaretakerException {
        MockMultipartFile employeeJson = new MockMultipartFile("employee", null,
                "application/json", "{\"name\": \"Emp Name\"}".getBytes());
        SendImageDto sendImageDto= new SendImageDto(employeeJson,"Vinay","PCM","73578dfd-e7c9-4381-a348-113e72d80fa2",123);
        User user= new User();
        List<MedicineHistory> medicineHistoryList= Collections.emptyList();
        UserMedicines userMedicines= new UserMedicines();
        when(userMedicineRepository.getMedicineById(123)).thenReturn(userMedicines);
        SendImageResponse imageResponse= careTakerServiceImpl.sendImageToCaretaker(employeeJson,"fyiaifkvaf","73578dfd-e7c9-4381-a348-113e72d80fa2","PCM",123);
        Assertions.assertEquals(Constants.FAILED,imageResponse.getStatus());

    }

    @Test
    @DisplayName("Send image Test")
    void sendImageToCaretakerTest() throws UserCaretakerException{
        MockMultipartFile employeeJson = new MockMultipartFile("employee", null,
                "application/json", "{\"name\": \"Emp Name\"}".getBytes());
        SendImageDto sendImageDto= new SendImageDto(employeeJson,"Vinay","PCM","73578dfd-e7c9-4381-a348-113e72d80fa2",123);
        User user= new User();
        List<MedicineHistory> medicineHistoryList= Collections.emptyList();
        UserMedicines userMedicines= new UserMedicines(123,new Date(),"PCM","something","Mon",new Date(),"10:00 AM","anything",12,5,LocalDateTime.now(),LocalDateTime.now(),user,medicineHistoryList,null);
        when(userMedicineRepository.getMedicineById(123)).thenReturn(userMedicines);
        SendImageResponse imageResponseTest = careTakerServiceImpl.sendImageToCaretaker(sendImageDto.getImage(),"Something","73578dfd-e7c9-4381-a348-113e72d80fa2","PCM",123);
        Assertions.assertEquals(Constants.SUCCESS,imageResponseTest.getStatus());
    }



}
