package com.example.user_service.batch;

import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.model.UserEntity;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.service.CareTakerServiceImpl;
import com.example.user_service.util.Datehelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
public class ScheduleNotifications {


    @Autowired
    UserMedicineRepository userMedicineRepository;
    @Autowired
    CareTakerServiceImpl careTakerService;
    Logger logger = LoggerFactory.getLogger(ScheduleNotifications.class);

 //   @Scheduled(fixedRate = 15000)
    public void sendNotificationsToCaretakers() {
        userMedicineRepository.getMedicinesforToday(Datehelper.getDay())
                .stream().filter(userMedicines -> userMedicines.getMedicineName() != null)
                .forEach(userMedicines -> {
                    UserEntity userEntity = userMedicines.getUserEntity();
                    String[] times = userMedicines.getTime().split("-");
                    for(String time : times){

                        int nh = 14;
                        int nm = 20;

                        int fh = Integer.parseInt(time.split(" ")[0].split(":")[0]);
                        int fm = Integer.parseInt(time.split(" ")[0].split(":")[1]);

                        if (fh <= nh && ((nm-fm) <=20 && (nm-fm) >= 0) ) {
                            logger.info(Arrays.deepToString(times));
                            try {
                                careTakerService.getMyCaretakers(userEntity.getUserId())
                                        .stream().forEach(userCaretaker -> {});

                            } catch (UserCaretakerException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                });


    }


}
//