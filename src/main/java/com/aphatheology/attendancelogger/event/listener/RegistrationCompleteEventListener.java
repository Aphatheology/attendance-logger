package com.aphatheology.attendancelogger.event.listener;


import com.aphatheology.attendancelogger.entity.StaffEntity;
import com.aphatheology.attendancelogger.event.RegistrationCompleteEvent;
import com.aphatheology.attendancelogger.service.AuthService;
import com.aphatheology.attendancelogger.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final AuthService authService;
    private final EmailService emailService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        StaffEntity staff = event.getStaff();
        String token = UUID.randomUUID().toString();

        this.authService.saveToken(staff, token, "VERIFICATION");

        String url = event.getApplicationUrl() + "/auth/verify?token=" + token;
        String emailBody = "Click the url to verify your account: " + url;

        this.emailService.sendEmail(staff.getEmail(), "Email Verification", emailBody);

        log.info(emailBody);
    }
}
