package com.aphatheology.attendancelogger.event;

import com.aphatheology.attendancelogger.entity.StaffEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private final StaffEntity staff;
    private final String applicationUrl;
    public RegistrationCompleteEvent(StaffEntity staff, String applicationUrl) {
        super(staff);
        this.staff = staff;
        this.applicationUrl = applicationUrl;
    }
}
