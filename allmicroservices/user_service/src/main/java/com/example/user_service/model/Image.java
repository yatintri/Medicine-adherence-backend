package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @NotNull(message = "ImageId is mandatory")
    @NotBlank(message = "ImageId is mandatory")
    private String imageId;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Date is mandatory")
    private Date date;

    @Column(name = "time")
    @NotNull(message = "Time is mandatory")
    @NotBlank(message = "Time is mandatory")
    private String time;

    @Column(name = "Caretaker_name")
    @NotNull(message = "Caretaker name is mandatory")
    @NotBlank(message = "Caretaker name is mandatory")
    private String caretakerName;

    @Column(name = "image_url")
    @NotNull(message = "ImageUrl is mandatory")
    @NotBlank(message = "ImageUrl is mandatory")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "medimage_id",
            referencedColumnName = "medicine_id"
    )
    @JsonIgnore
    UserMedicines userMedicines;

}
//
