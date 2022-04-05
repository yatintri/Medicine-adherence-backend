package com.example.user_service.pojos;

import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;

import java.util.List;

public class UserProfileResponse {

     private String status;
     private List<UserEntity> userEntityList;
     private List<UserMedicines> medicinesList;

     public UserProfileResponse(String status , List<UserEntity> userDetailsList ,
                                List<UserMedicines> medicinesList){

          this.status = status;
          this.userEntityList = userDetailsList;
          this.medicinesList = medicinesList;

     }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UserEntity> getUserEntityList() {
        return userEntityList;
    }

    public void setUserEntityList(List<UserEntity> userEntityList) {
        this.userEntityList = userEntityList;
    }

    public List<UserMedicines> getMedicinesList() {
        return medicinesList;
    }

    public void setMedicinesList(List<UserMedicines> medicinesList) {
        this.medicinesList = medicinesList;
    }
}
