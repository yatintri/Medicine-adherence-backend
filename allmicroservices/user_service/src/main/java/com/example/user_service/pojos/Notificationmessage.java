package com.example.user_service.pojos;

public class Notificationmessage {
//
    private String fcm_token;
    private String message;
    private String title;
    private String body;
    private String image_url;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Notificationmessage(String fcm_token, String message , String title , String body , String image_url) {
        this.fcm_token = fcm_token;
        this.message = message;
        this.title = title;
        this.body = body;
        this.image_url = image_url;

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

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
