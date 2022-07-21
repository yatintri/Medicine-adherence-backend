package com.example.user_service.pojos.dto.response.user;

import com.example.user_service.model.User;
import com.example.user_service.model.UserMedicines;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
/**
 * This is a response class for list of User and medicines
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

     private String status;
     private List<User> userEntityList;
     private List< UserMedicines> medicinesList;

}
