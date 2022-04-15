package com.example.user_service.config;

import com.example.user_service.model.UserMedicines;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

@Component
public class PdfMailSender {

    @Autowired
    private JavaMailSender javaMailSender;
    Logger logger = LoggerFactory.getLogger(PdfMailSender.class);

    @Async
    public void send(String receiver , List<UserMedicines> userMedicinesList) throws  FileNotFoundException, DocumentException {

        logger.info(Thread.currentThread().getName());
        Document document = new Document();
        PdfWriter.getInstance(document , new FileOutputStream(System.getProperty("user.dir")+"/src/main/upload/static/pdf/sample.pdf"));
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

        userMedicinesList.forEach(userMedicines -> {
            Paragraph paragraph = new Paragraph();
            Chunk chunk = new Chunk(userMedicines.getMedicineName() +" "+ userMedicines.getMedicineDes() +" "+(((double)userMedicines.getCurrentCount()/userMedicines.getTotalMedReminders())*100)+" % ", font);
            paragraph.add(chunk);
            try {
                document.add(Chunk.NEWLINE);
                document.add(paragraph);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        document.close();
        MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                helper.setFrom("medstickapp@gmail.com");
                helper.setTo(receiver);
                helper.setSubject("Report");
                helper.setText("Report from patient");

                FileSystemResource file
                        = new FileSystemResource(new File(System.getProperty("user.dir")+"/src/main/upload/static/pdf/sample.pdf"));
                helper.addAttachment("report.pdf", file);

            }
        };

        javaMailSender.send(mimeMessagePreparator);





    }

}
