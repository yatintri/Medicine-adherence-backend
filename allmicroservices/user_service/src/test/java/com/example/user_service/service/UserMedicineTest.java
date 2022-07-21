package com.example.user_service.service;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.Image;
import com.example.user_service.model.MedicineHistory;
import com.example.user_service.model.User;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.request.MedicineHistoryDTO;
import com.example.user_service.pojos.dto.request.MedicinePojo;
import com.example.user_service.pojos.dto.response.image.ImageListResponse;
import com.example.user_service.pojos.dto.response.medicine.MedicineResponse;
import com.example.user_service.pojos.dto.response.medicine.SyncResponse;
import com.example.user_service.repository.ImageRepository;
import com.example.user_service.repository.UserMedHistoryRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.impl.UserMedicineServiceImpl;
import com.example.user_service.util.Constants;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
 class UserMedicineTest {

    UserMedicineServiceImpl userMedicineServiceImpl;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMedicineRepository userMedicineRepository;

    @Mock
    ImageRepository imageRepository;

    @Mock
    UserMedHistoryRepository userMedHistoryRepository;

    @BeforeEach
    public void init(){
        userMedicineServiceImpl= new UserMedicineServiceImpl(userRepository,userMedicineRepository,imageRepository,userMedHistoryRepository);
    }

    @Test
    @DisplayName("Get all medicine Exception Test")
    void getAllUserMedicinesExceptionTest(){
        when(userRepository.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(null);
        try{
            userMedicineServiceImpl.getallUserMedicines("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2);
        }catch (UserExceptionMessage | UserMedicineException userExceptionMessage){
            Assertions.assertEquals("Please provide valid id",userExceptionMessage.getMessage());
        }
    }

    @Test
    @DisplayName("Get all medicine SqlException Test")
    void getAllUserMedicinesSqlExceptionTest(){
        when(userRepository.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenThrow(JDBCConnectionException.class);
        try{
            userMedicineServiceImpl.getallUserMedicines("73578dfd-e7c9-4381-a348-113e72d80fa2",0,2);
        }catch (UserExceptionMessage | UserMedicineException userExceptionMessage){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userExceptionMessage.getMessage());
        }
    }

    @Test
    @DisplayName("Get all medicine Test")
    void getAllUserMedicinesTest() throws UserExceptionMessage, UserMedicineException, InterruptedException, ExecutionException {
        UserMedicines userMedicines= new UserMedicines();
        List<UserMedicines> userMedicines1= new ArrayList<>();
        userMedicines1.add(userMedicines);
        User user = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,userMedicines1);
        when(userRepository.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(user);
        CompletableFuture<List<UserMedicines>> listCompletableFuture= userMedicineServiceImpl.getallUserMedicines("73578dfd-e7c9-4381-a348-113e72d80fa2",0,1);
        Assertions.assertEquals(1,listCompletableFuture.get().size());
    }
    @Test
    @DisplayName("Sync data Exception Test")
    void synDataException() throws UserMedicineException {
        List<MedicinePojo> medicinePojoList = new ArrayList<>();
        List<UserMedicines> userMedicines1= new ArrayList<>();
        User user = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,userMedicines1);
        when(userRepository.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(user);
        try{
            userMedicineServiceImpl.syncData("73578dfd-e7c9-4381-a348-113e72d80fa2",medicinePojoList);
        }catch (UserMedicineException userMedicineException){
            Assertions.assertEquals("Unable to sync",userMedicineException.getMessage());
        }
    }

    @Test
    @DisplayName("Sync data Sql Exception Test")
    void synDataSqlException() throws UserMedicineException {
        List<MedicinePojo> medicinePojoList = new ArrayList<>();
        List<UserMedicines> userMedicines1= new ArrayList<>();
        User user = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,userMedicines1);
        when(userRepository.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenThrow(JDBCConnectionException.class);
        try{
            userMedicineServiceImpl.syncData("73578dfd-e7c9-4381-a348-113e72d80fa2",medicinePojoList);
        }catch (UserMedicineException userMedicineException){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userMedicineException.getMessage());
        }
    }
    @Test
    @DisplayName("Sync data Test")
    void syncDataTest() throws UserMedicineException {
        MedicinePojo medicinePojo= new MedicinePojo(123,"Mon",1,null,"something",10,"PCM","something",null,0,"10:00 AM");
        List<MedicinePojo> medicinePojoList = new ArrayList<>();
        medicinePojoList.add(medicinePojo);
        List<MedicineHistory> medicineHistoryList = new ArrayList<>();
        List<UserMedicines> userMedicinesList= new ArrayList<>();
        User user = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,userMedicinesList);
        UserMedicines userMedicines= new UserMedicines(123,new Date(),"PCM","something","Mon",new Date(),"10:00 AM","anything",12,5,LocalDateTime.now(),LocalDateTime.now(),user,medicineHistoryList,null);
        userMedicinesList.add(userMedicines);
        MedicineHistory medicineHistory= new MedicineHistory(123,null,"10:00 AM",null,LocalDateTime.now(),LocalDateTime.now(),null);
        medicineHistoryList.add(medicineHistory);
        when(userRepository.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(user);
        SyncResponse value =userMedicineServiceImpl.syncData("73578dfd-e7c9-4381-a348-113e72d80fa2",medicinePojoList);
        Assertions.assertEquals(Constants.SUCCESS,value.getStatus());
    }
    @Test
    @DisplayName("Sync History Exception Test")
    void syncMedicineHistoryException(){
        when(userMedicineRepository.getMedicineById(123)).thenReturn(null);
        try{
            userMedicineServiceImpl.syncMedicineHistory(123,null);
        }catch (UserMedicineException e){
            Assertions.assertEquals("Unable to sync",e.getMessage());
        }
    }

    @Test
    @DisplayName("Sync History SqlException Test")
    void syncMedicineHistorySqlException(){
        when(userMedicineRepository.getMedicineById(123)).thenThrow(JDBCConnectionException.class);
        try{
            userMedicineServiceImpl.syncMedicineHistory(123,null);
        }catch (UserMedicineException e){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,e.getMessage());
        }
    }
    @Test
    @DisplayName("Sync History Test")
    void syncMedicineHistoryTest() throws UserMedicineException {
        User user = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,null);
        MedicineHistory medicineHistory= new MedicineHistory(123,null,"10:00 AM",null,LocalDateTime.now(),LocalDateTime.now(),null);
        List<MedicineHistory> medicineHistoryList = new ArrayList<>();
        medicineHistoryList.add(medicineHistory);
        UserMedicines userMedicines= new UserMedicines(123,new Date(),"PCM","something","Mon",new Date(),"10:00 AM","anything",12,5,LocalDateTime.now(),LocalDateTime.now(),user,medicineHistoryList,null);
        when(userMedicineRepository.getMedicineById(123)).thenReturn(userMedicines);
        String[] timing= new String[4];
        timing[0]="10:00 AM";
        timing[1]="12:00 PM";
        MedicineHistoryDTO medicineHistoryDTO= new MedicineHistoryDTO(1234,new Date(), timing,timing);
        MedicineHistoryDTO medicineHistoryDTO1= new MedicineHistoryDTO(1235,new Date(),timing,timing);
        MedicineHistoryDTO medicineHistoryDTO2= new MedicineHistoryDTO(1236,new Date(),timing,timing);
        MedicineHistoryDTO medicineHistoryDTO3= new MedicineHistoryDTO(1237,new Date(),timing,timing);
        List<MedicineHistoryDTO> medicineHistoryDTOList= new ArrayList<>();
        medicineHistoryDTOList.add(medicineHistoryDTO);
        medicineHistoryDTOList.add(medicineHistoryDTO1);
        medicineHistoryDTOList.add(medicineHistoryDTO2);
        medicineHistoryDTOList.add(medicineHistoryDTO3);
        MedicineResponse medicineResponse= userMedicineServiceImpl.syncMedicineHistory(123,medicineHistoryDTOList);
        Assertions.assertEquals(medicineHistoryDTOList.size(),medicineResponse.getUserMedicinesList().size());
    }

    @Test
    @DisplayName("Get History  Test")
    void getMedicineHistoryTest() throws UserMedicineException {
        User user = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,null);
        MedicineHistory medicineHistory= new MedicineHistory(123,null,"10:00 AM",null,LocalDateTime.now(),LocalDateTime.now(),null);
        List<MedicineHistory> medicineHistoryList = new ArrayList<>();
        medicineHistoryList.add(medicineHistory);
        UserMedicines userMedicines= new UserMedicines(123,new Date(),"PCM","something","Mon",new Date(),"10:00 AM","anything",12,5,LocalDateTime.now(),LocalDateTime.now(),user,medicineHistoryList,null);
        when(userMedicineRepository.getMedicineById(123)).thenReturn(userMedicines);
        MedicineResponse medicineResponse= userMedicineServiceImpl.getMedicineHistory(123,0,4);
        Assertions.assertEquals(medicineHistoryList.size(),medicineResponse.getUserMedicinesList().size());
    }

    @Test
    @DisplayName("Get History Exception Test")
    void getMedicineHistoryException(){
        User user= new User();
        List<MedicineHistory> medicineHistoryList= Collections.emptyList();
        UserMedicines userMedicines= new UserMedicines(123,new Date(),"PCM","something","Mon",new Date(),"10:00 AM","anything",12,5,LocalDateTime.now(),LocalDateTime.now(),user,medicineHistoryList,null);
        when(userMedicineRepository.getMedicineById(123)).thenReturn(userMedicines);
        try{
            userMedicineServiceImpl.getMedicineHistory(123,0,1);
        }catch (UserMedicineException e){
            Assertions.assertEquals("Data not found",e.getMessage());
        }
    }

    @Test
    @DisplayName("Get History SqlException Test")
    void getMedicineHistorySqlException(){
        User user= new User();
        List<MedicineHistory> medicineHistoryList= Collections.emptyList();
        UserMedicines userMedicines= new UserMedicines(123,new Date(),"PCM","something","Mon",new Date(),"10:00 AM","anything",12,5,LocalDateTime.now(),LocalDateTime.now(),user,medicineHistoryList,null);
        when(userMedicineRepository.getMedicineById(123)).thenThrow(JDBCConnectionException.class);
        try{
            userMedicineServiceImpl.getMedicineHistory(123,0,1);
        }catch (UserMedicineException e){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,e.getMessage());
        }
    }
    @Test
    @DisplayName("Get Medicine images Test")
    void getUserMedicineImagesTest() throws  UserMedicineException {
        User user= new User();
        Image image= new Image();
        List<Image> imageList= new ArrayList<>();
        imageList.add(image);
        List<MedicineHistory> medicineHistoryList= Collections.emptyList();
        UserMedicines userMedicines= new UserMedicines(123,new Date(),"PCM","something","Mon",new Date(),"10:00 AM","anything",12,5,LocalDateTime.now(),LocalDateTime.now(),user,medicineHistoryList,imageList);
        when(userMedicineRepository.getMedicineById(123)).thenReturn(userMedicines);
        ImageListResponse imageList1= userMedicineServiceImpl.getUserMedicineImages(123,0,2);
        Assertions.assertEquals(imageList.size(),imageList1.getImageList().size());
    }

    @Test
    @DisplayName("Get Medicine images Sql Test")
    void getUserMedicineImagesSqlTest() throws  UserMedicineException {
        User user = new User();
        Image image = new Image();
        List<Image> imageList = new ArrayList<>();
        imageList.add(image);
        List<MedicineHistory> medicineHistoryList = Collections.emptyList();
        UserMedicines userMedicines = new UserMedicines(123, new Date(), "PCM", "something", "Mon", new Date(), "10:00 AM", "anything", 12, 5,LocalDateTime.now(),LocalDateTime.now(), user, medicineHistoryList, imageList);
        when(userMedicineRepository.getMedicineById(123)).thenThrow(JDBCConnectionException.class);
        try {
            userMedicineServiceImpl.getUserMedicineImages(123, 0, 2);
        } catch (UserMedicineException userMedicineException) {
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userMedicineException.getMessage());
        }
    }
}
