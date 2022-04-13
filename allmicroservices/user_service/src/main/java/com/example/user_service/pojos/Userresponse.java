package com.example.user_service.pojos;

import com.example.user_service.model.UserEntity;

import java.util.ArrayList;
import java.util.List;
public class Userresponse {

    private String status;
    private List<UserEntity> userentity = new ArrayList<>();

    private String jwtToken;

    public Userresponse(String status, UserEntity userentity , String jwtToken) {
        this.status = status;
        this.userentity.add(userentity);
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UserEntity> getUserentity() {
        return userentity;
    }

    public void setUserentity(List<UserEntity> userentity) {
        this.userentity = userentity;
    }
}
//