package com.example.user_service.pojos;

public class Notificationmessage {
//
    private String fcm_token;
    private String message;

    public Notificationmessage(String fcm_token, String message) {
        this.fcm_token = fcm_token;
        this.message = message;
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
