package com.example.user_service.config;

import com.example.user_service.model.MedicineHistory;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.itextpdf.html2pdf.HtmlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;


@Component
public class PdfMailSender {

    @Autowired
    private JavaMailSender javaMailSender;
    Logger logger = LoggerFactory.getLogger(PdfMailSender.class);


    public String send(UserEntity userEntity, UserMedicines userMedicines
            , List<MedicineHistory> medicineHistoryList) {
        UserDetails userDetails = userEntity.getUserDetails();
        final String[] filepath = new String[1];
        final String[] HTML = new String[1];
        HTML[0] = "<div style='padding:10px;height: 100%; border-color: #3743ab; border-width: 3px;border-style: solid;border-radius: 10px;padding-left: 20px;padding-right: 20px;'>\n" +
                "            <div style='text-align: right;'><img src='MEdstick.png' style='width:150px; height:60px'></div>\n" +
                "            <div style='background-color: #3743ab;border-radius: 15px; margin-bottom: 30px;height: 110px;'>\n" +
                "                <div style='font-size: 60px;text-align: center;color: white;'>Patient Report</div>\n" +
                "                <div style='font-size: 30px;text-align: center; color:white;'>Adherence Rate - 75%</div>\n" +
                "            </div>\n" +
                "           <div style='font-size:24px; margin-bottom: 8px; font-weight: 600; color: gray;'>Patient Details </div>\n" +
                "            <div align='left'>\n" +
                "                 <div>Name - "+userEntity.getUserName()+"</div>\n" +
                "                 <div>Age - "+userDetails.getAge()+"</div>\n" +
                "                 <div>Blood Group - "+userDetails.getBloodGroup()+"</div>      \n" +
                "                  <div>Marital Status - Unmarried</div>\n" +
                "                  <div>Gender - "+userDetails.getGender()+"</div>\n" +
                "                  <div>Contact - "+userDetails.getUsercontact()+"</div>\n" +
                "            </div><br>\n" +
                "            <div style='font-size:24px; margin-bottom: 8px;font-weight: 600; color: gray;'>Medicine Details </div>\n" +
                "                  <span>Name - " + userMedicines.getMedicineName() + "</span><br>\n" +
                "             <span>Description -" + userMedicines.getMedicineDes() + "</span><br>\n" +
                "                 <span>Start Date - " + userMedicines.getStartDate() + "</span><br>\n" +
                "                  <span>End Date - " + userMedicines.getEndDate() + "</span><br>\n" +
                "                  <span>Days - " + userMedicines.getDays() + "</span><br>\n" +
                "                  <span>Timings - " + userMedicines.getTime() + "</span><br>\n" +
                "            <br>\n" +
                "            <div>\n" +
                "                <div style='padding-bottom: 8px;font-size: 24px;font-weight: 600; color: gray;'>\n" +
                "                    Detail Report of Patient\n" +
                "                </div>\n" +
                "                <table style=\"width: 100%;\">\n" +

                "            <tr>\n" +
                "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;height: 40px;'>Date</td>\n" +
                "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;color: green;height: 40px;'>Taken</td>\n" +
                "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;color: red;height: 40px;'>Not Taken</td>\n" +
                "            </tr>\n" +

                medicineHistory(medicineHistoryList) +
                "            </table>\n" +
                "            </div>\n" +
                "            </div>";

        try {
            filepath[0] = UUID.randomUUID().toString();
            HtmlConverter.convertToPdf(HTML[0], new FileOutputStream(System.getProperty("user.dir") + "/src/main/upload/static/pdf/" + filepath[0] + ".pdf"));
            return filepath[0];
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }


    private String medicineHistory(List<MedicineHistory> medicineHistories) {
        String medicineHistory = "";

        for (MedicineHistory medicineHistory1 : medicineHistories) {

            medicineHistory += " <tr>\n" +
                    "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;height: 40px;'>" + medicineHistory1.getDate() + "</td>\n" +
                    "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;height: 40px;'>" + medicineHistory1.getTaken() + "</td>\n" +
                    "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;height: 40px;'>" + medicineHistory1.getNottaken() + "</td>\n" +
                    "            </tr>\n";

        }

        return medicineHistory;
    }


}