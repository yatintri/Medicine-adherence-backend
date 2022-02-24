package com.example.user_service.service;

import com.example.user_service.exception.UserexceptionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.SendFailedException;

@Component
public class MailService {

  @Autowired
 private JavaMailSender javaMailSender;

  public void sendEmail(String email) throws UserexceptionMessage {

      try{
          SimpleMailMessage message = new SimpleMailMessage();
          message.setTo(email);
          message.setSubject("Caretaker request");
          message.setText("Please join through thiss link");
          javaMailSender.send(message);

      }catch (Exception e){
          throw new UserexceptionMessage("Invalid email");
      }
  }
}
