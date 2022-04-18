package com.example.user_service.config;

import com.example.user_service.model.UserMedicines;
import com.itextpdf.html2pdf.HtmlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
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

    @Async
    public void send(String receiver , List<UserMedicines> userMedicinesList){

        userMedicinesList.forEach(userMedicines -> {
            final String HTML="<div style='padding:10px;height: 100%; border-color: #3743ab; border-width: 3px;border-style: solid;border-radius: 10px;padding-left: 20px;padding-right: 20px;'>\n" +
                    "            <div style='text-align: right;'><img src='MEdstick.png' style='width:150px; height:60px'></div>\n" +
                    "            <div style='background-color: #3743ab;border-radius: 15px; margin-bottom: 30px;height: 110px;'>\n" +
                    "                <div style='font-size: 60px;text-align: center;color: white;'>Patient Report</div>\n" +
                    "                <div style='font-size: 30px;text-align: center; color:white;'>Adherence Rate - 75%</div>\n" +
                    "            </div>\n" +
                    "           <div style='font-size:24px; margin-bottom: 8px; font-weight: 600; color: gray;'>Patient Details </div>\n" +
                    "            <div align='left'>\n" +
                    "                 <div>Name - Vinay Kumar Soni</div>\n" +
                    "                 <div>Age - 20</div>\n" +
                    "                 <div>Blood Group - AB+</div>      \n" +
                    "                  <div>Marital Status - Unmarried</div>\n" +
                    "                  <div>Gender - Male</div>\n" +
                    "                  <div>Contact - 8725052854</div>\n" +
                    "            </div><br>\n" +
                    "            <div style='font-size:24px; margin-bottom: 8px;font-weight: 600; color: gray;'>Medicine Details </div>\n" +
                    "                  <span>Name - "+userMedicines.getMedicineName()+"</span><br>\n" +
                    "             <span>Description -"+userMedicines.getMedicineDes()+"</span><br>\n" +
                    "                 <span>Start Date - "+userMedicines.getStartDate()+"</span><br>\n" +
                    "                  <span>End Date - "+userMedicines.getEndDate()+"</span><br>\n" +
                    "                  <span>Days - "+userMedicines.getDays()+"</span><br>\n" +
                    "                  <span>Timings - "+userMedicines.getTime()+"</span><br>\n" +
                    "            <br>\n" +
                    "            <div>\n" +
                    "                <div style='padding-bottom: 8px;font-size: 24px;font-weight: 600; color: gray;'>\n" +
                    "                    Detail Report of Patient\n" +
                    "                </div>\n" +
                    "                <table style=\"width: 100%;\">\n" +
                    "            <tr>\n" +
                    "               <th style='border: 0.5px solid black;height: 30px;'>Dates</th>\n" +
                    "               <th style='border: 0.5px solid black;height: 30px;color:green'>Taken</th>\n" +
                    "               <th style='border: 0.5px solid black;height: 30px;color:red'>Not Taken</th>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;height: 40px;'>12-04-2022</td>\n" +
                    "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;height: 40px;'>8:30 AM, 11:00 AM, 4:00 PM</td>\n" +
                    "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;height: 40px;'></td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;height: 40px;'>14-04-2022</td>\n" +
                    "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;height: 40px;'>9:00 AM</td>\n" +
                    "               <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;height: 40px;'>1:00 PM, 6:30 PM</td>\n" +
                    "            </tr>\n" +
                    "            </table>\n" +
                    "            </div>\n" +
                    "            </div>";

            try {
                String filepath = UUID.randomUUID().toString();
                HtmlConverter.convertToPdf(HTML, new FileOutputStream(System.getProperty("user.dir")+"/src/main/upload/static/pdf/"+filepath+".pdf"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        });

    }

}