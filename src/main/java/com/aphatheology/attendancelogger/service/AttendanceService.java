package com.aphatheology.attendancelogger.service;

import com.aphatheology.attendancelogger.dto.AttendanceDto;
import com.aphatheology.attendancelogger.dto.ClockInDto;
import com.aphatheology.attendancelogger.dto.ClockOutDto;
import com.aphatheology.attendancelogger.entity.AttendanceEntity;
import com.aphatheology.attendancelogger.entity.StaffEntity;
import com.aphatheology.attendancelogger.exception.ResourceNotFoundException;
import com.aphatheology.attendancelogger.repository.AttendanceRepository;
import com.aphatheology.attendancelogger.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StaffRepository staffRepository;


    public AttendanceDto map2Dto(AttendanceEntity attendance) {
        AttendanceDto attendanceDto = new AttendanceDto();
        attendanceDto.setId(attendance.getId());
        attendanceDto.setStaffId(attendance.getStaff().getId());
        attendanceDto.setDay(attendance.getDay());
        attendanceDto.setTimeIn(attendance.getTimeIn());
        attendanceDto.setTimeOut(attendance.getTimeOut());
        return attendanceDto;
    }

    public List<AttendanceDto> getAllAttendances() {
        List<AttendanceEntity> allAttendance = this.attendanceRepository.findAll();

        return  allAttendance.stream().map(this::map2Dto).toList();
    }

    public AttendanceDto getAttendanceById(Long id) {
        AttendanceEntity attendance = this.attendanceRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Attendance Not Found"));

        return map2Dto(attendance);
    }

    public AttendanceDto clockIn(ClockInDto attendance) {
        StaffEntity staff = this.staffRepository.findById(attendance.getStaffId()).orElseThrow(() ->
                new ResourceNotFoundException("Staff Not Found"));

        AttendanceEntity attendanceEntity = new AttendanceEntity();
        attendanceEntity.setTimeIn(attendance.getTimeIn());
        attendanceEntity.setDay(attendance.getDay());
        attendanceEntity.setStaff(staff);

        this.attendanceRepository.save(attendanceEntity);

        return map2Dto(attendanceEntity);
    }

    public AttendanceDto clockOut(Long attendanceId, ClockOutDto clockOut) {
        AttendanceEntity attendance = this.attendanceRepository.findById(attendanceId).orElseThrow(() ->
                new ResourceNotFoundException("Attendance Not Found"));

        attendance.setTimeOut(clockOut.getTimeOut());

        this.attendanceRepository.save(attendance);

        return map2Dto(attendance);
    }

    public List<AttendanceDto> getAllAttendancesByDay(LocalDate day) {
        List<AttendanceEntity> attendances = this.attendanceRepository.findAllByDay(day);

        return  attendances.stream().map(this::map2Dto).toList();
    }
}
