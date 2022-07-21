package com.example.user_service.config;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.user_service.model.MedicineHistory;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.User;
import com.example.user_service.model.UserMedicines;

import com.itextpdf.html2pdf.HtmlConverter;

import static com.example.user_service.util.Constants.*;

/**
 * This is config class to create pdf
 */
@Component
public class PdfMailSender {


    private StringBuilder medicineHistory(List<MedicineHistory> medicineHistories) {
        StringBuilder medicineHistory = new StringBuilder();

        for (MedicineHistory medicineHistory1 : medicineHistories) {
            medicineHistory.append(" <tr>\n" + STYLE)
                    .append(medicineHistory1.getDate())
                    .append(TD)
                    .append(STYLE)
                    .append(medicineHistory1.getTaken())
                    .append(TD)
                    .append(STYLE)
                    .append(medicineHistory1.getNotTaken())
                    .append(TD)
                    .append("</tr>\n");
        }

        return medicineHistory;
    }

    public String send(User userEntity, UserMedicines userMedicines, List<MedicineHistory> medicineHistoryList)
            throws FileNotFoundException {
        UserDetails userDetails = userEntity.getUserDetails();
        final String[] filepath = new String[1];
        final String[] html = new String[1];

        html[0] =
                "<div style='padding:10px;height: 100%; border-color: #3743ab; border-width: 3px;border-style: solid;border-radius: 10px;padding-left: 20px;padding-right: 20px;'>\n"
                        + "            <div style='text-align: right;'><img src='MEdstick.png' style='width:150px; height:60px'></div>\n"
                        + "            <div style='background-color: #3743ab;border-radius: 15px; margin-bottom: 30px;height: 110px;'>\n"
                        + "                <div style='font-size: 60px;text-align: center;color: white;'>Patient Report</div>\n"
                        + "                <div style='font-size: 30px;text-align: center; color:white;'>Adherence Rate - "
                        + (Math.ceil(((double) userMedicines.getCurrentCount() / userMedicines.getTotalMedReminders()) * 100))
                        + "%" + DIV + "            </div>\n"
                        + "           <div style='font-size:24px; margin-bottom: 8px; font-weight: 600; color: gray;'>Patient Details </div>\n"
                        + "            <div align='left'>\n" + "                 <div>Name - " + userEntity.getUserName() + DIV
                        + "                 <div>Age - " + userDetails.getAge() + DIV + "                 <div>Blood Group - "
                        + userDetails.getBloodGroup() + DIV + "                  <div>Marital Status - Unmarried</div>\n"
                        + "                  <div>Gender - " + userDetails.getGender() + DIV + "                  <div>Contact - "
                        + userDetails.getUserContact() + DIV + "            </div><br>\n"
                        + "            <div style='font-size:24px; margin-bottom: 8px;font-weight: 600; color: gray;'>Medicine Details </div>\n"
                        + "                  <span>Name - " + userMedicines.getMedicineName() + SPAN
                        + "             <span>Description -" + userMedicines.getMedicineDes() + SPAN
                        + "                 <span>Start Date - " + userMedicines.getStartDate() + SPAN
                        + "                  <span>End Date - " + userMedicines.getEndDate() + SPAN
                        + "                  <span>Days - " + userMedicines.getDays() + SPAN + "                  <span>Timings - "
                        + userMedicines.getTime() + SPAN + "            <br>\n" + "            <div>\n"
                        + "                <div style='padding-bottom: 8px;font-size: 24px;font-weight: 600; color: gray;'>\n"
                        + "                    Detail Report of Patient\n" + "                </div>\n"
                        + "                <table style=\"width: 100%;\">\n" + "            <tr>\n"
                        + "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;height: 40px;'>Date</td>\n"
                        + "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;color: green;height: 40px;'>Taken</td>\n"
                        + "                <td style='border: 0.5px solid black;text-align: center;vertical-align: middle;color: red;height: 40px;'>Not Taken</td>\n"
                        + "            </tr>\n" + medicineHistory(medicineHistoryList) + "            </table>\n"
                        + "            </div>\n" + "            </div>";

        try {
            filepath[0] = UUID.randomUUID().toString();
            HtmlConverter.convertToPdf(html[0],
                    new FileOutputStream(System.getProperty("user.dir")
                            + "/src/main/upload/static/pdf/" + filepath[0] + ".pdf"));

            return filepath[0];
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found");
        }
    }
}
