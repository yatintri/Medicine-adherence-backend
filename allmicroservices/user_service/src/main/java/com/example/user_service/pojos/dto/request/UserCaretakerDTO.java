package com.example.user_service.pojos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
/**
 * This is a Pojo class for Caretaker
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCaretakerDTO {
    @NotBlank(message = "Patient Name is mandatory")
    @NotNull(message = "Patient Name is mandatory")
    private String patientName;

    private boolean reqStatus;

    @NotBlank(message = "Caretaker Id is mandatory")
    @NotNull(message = "Caretaker Id is mandatory")
    private String caretakerId;

    @NotBlank(message = "Patient Id is mandatory")
    @NotNull(message = "Patient Id is mandatory")
    private String patientId;

    @NotBlank(message = "Caretaker Name is mandatory")
    @NotNull(message = "Caretaker Name is mandatory")
    private String caretakerUsername;

    private LocalDateTime createdAt;

    @NotBlank(message = "Sent by is mandatory")
    @NotNull(message = "Sent by is mandatory")
    private String sentBy;

}
//