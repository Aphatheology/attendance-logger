package com.aphatheology.attendancelogger.event.listener;


import com.aphatheology.attendancelogger.entity.StaffEntity;
import com.aphatheology.attendancelogger.event.RegistrationCompleteEvent;
import com.aphatheology.attendancelogger.service.AuthService;
import com.aphatheology.attendancelogger.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

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
        String appBaseUrl = event.getApplicationUrl();

        String url = appBaseUrl + "/auth/verify?token=" + token;
        String emailBody = "Click the url to verify your account: " + url;
        String emailBasicHtmlBody = "<h1>Hello,</h1>" +
                "<p>Click the url to verify your account: " + url + "</p>";

//        this.emailService.sendEmail(staff.getEmail(), "Email Verification", emailBody);

//        this.emailService.sendBasicHtmlEmail(staff.getEmail(), "Email Verification", emailBasicHtmlBody);


        Context context = new Context();
        context.setVariable("staffName", staff.getFullname());
        context.setVariable("appUrl", appBaseUrl);
        context.setVariable("token", token);
        this.emailService.sendVerificationEmailWithHtmlTemplate(staff.getEmail(), "Email Verification", "email-verification-template", context);

        log.info(emailBody);
    }
}
