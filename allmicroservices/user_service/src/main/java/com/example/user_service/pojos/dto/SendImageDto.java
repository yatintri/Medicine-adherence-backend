package com.example.user_service.pojos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendImageDto {

    @NotNull(message = "Image is mandatory")
    private MultipartFile image;

    @NotBlank(message = "Name is mandatory")
    @NotNull(message = "Name is mandatory")
    private String name; //filename name

    @NotBlank(message = "MedName is mandatory")
    @NotNull(message = "MedName is mandatory")
    private String medName;

    @NotBlank(message = "Id is mandatory")
    @NotNull(message = "Id is mandatory")
    private String id; //caretaker id

    @NotBlank(message = "MedId is mandatory")
    @NotNull(message = "MedId is mandatory")
    private Integer medId;
}
