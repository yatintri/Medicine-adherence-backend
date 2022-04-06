package com.example.user_service.pojos;

public class Notificationmessage {
    //
    private String fcmToken;
    private String message;
    private String title;
    private String body;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Notificationmessage(String fcmToken, String message , String title , String body , String imageUrl) {
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