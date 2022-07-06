package com.example.user_service.pojos;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MailInfo {

    @Email
    @NotNull
    @NotBlank
    private String receiverMail;
    @NotBlank
    @NotNull
    private String mailMessage;
    @NotBlank
    @NotNull
    private String mailSubject;
    @NotBlank
    @NotNull
    private String sender;

    public MailInfo(String receiverMail ,
                    String mailMessage , String mailSubject,String sender){

        this.receiverMail = receiverMail;
        this.mailMessage = mailMessage;
        this.mailSubject = mailSubject;
        this.sender = sender;
    }

    public MailInfo() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiverMail(String receiverMail) {
        this.receiverMail = receiverMail;
    }

    public void setMailMessage(String mailMessage) {
        this.mailMessage = mailMessage;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getReceiverMail() {
        return receiverMail;
    }

    public String getMailMessage() {
        return mailMessage;
    }

    public String getMailSubject() {
        return mailSubject;
    }
}
//