package com.example.user_service.pojos.response;

import com.example.user_service.model.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageListResponse {

        private String status;
        private String message;
        private List<Image> imageList;

}
