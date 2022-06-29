package com.example.user_service.pojos.response;

import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

     private String status;
     private List< UserEntity> userEntityList;
     private List< UserMedicines> medicinesList;

}
///
