package com.example.user_service.pojos.response.caretaker;

import com.example.user_service.model.UserCaretaker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * This is response class to return total page,current page and total items
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaretakerResponsePage {

    private String status;
    private String message;
    private long totalItems;
    private int totalPage;
    private int currentPage;
    private List<UserCaretaker> userCaretakerStream;
}
