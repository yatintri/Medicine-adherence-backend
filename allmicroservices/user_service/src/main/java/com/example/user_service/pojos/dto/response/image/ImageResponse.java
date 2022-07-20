package com.example.user_service.pojos.response.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This is a response class for image
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {

    private String status;
    private String message;

}
