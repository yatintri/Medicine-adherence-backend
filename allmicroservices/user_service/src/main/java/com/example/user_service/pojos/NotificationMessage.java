package com.example.user_service.pojos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
 * This is a pojo class for sending notifications using message broker
 */
public class NotificationMessage {

    @NotNull
    @NotBlank
    private String fcmToken;
    @NotNull
    @NotBlank
    private String message;
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotBlank
    private String body;
    @NotNull
    @NotBlank
    private String imageUrl;
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public NotificationMessage(String fcmToken, String message , String title , String body , String imageUrl) {
        this.fcmToken = fcmToken;
        this.message = message;
        this.title = title;
        this.body = body;
        this.imageUrl = imageUrl;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
//