package com.example.user_service.pojos.request;

import com.example.user_service.validators.ImageValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
 * This is a Pojo class for Image
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendImageDto {

    @NotNull(message = "Image is mandatory")
    @ImageValidator
    private MultipartFile image;

    @NotBlank(message = "Name is mandatory")
    @NotNull(message = "Name is mandatory")
    private String name; //filename name

    @NotBlank(message = "MedName is mandatory")
    @NotNull(message = "MedName is mandatory")
    private String medicineName;

    @NotBlank(message = "Id is mandatory")
    @NotNull(message = "Id is mandatory")
    private String id; //caretaker id

    @NotNull(message = "MedId is mandatory")
    private Integer medicineId;
}
