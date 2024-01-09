package com.aphatheology.attendancelogger.controller;


import com.aphatheology.attendancelogger.dto.*;
import com.aphatheology.attendancelogger.service.AttendanceService;
import com.aphatheology.attendancelogger.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<AttendanceDto>> getAllAttendances() {
        return new ResponseEntity<>(attendanceService.getAllAttendances(), HttpStatus.OK);
    }

    @GetMapping("/day")
    public ResponseEntity<List<AttendanceDto>> getAllAttendancesByDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return new ResponseEntity<>(attendanceService.getAllAttendancesByDay(day), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AttendanceDto> clockInAttendance(@RequestBody @Valid ClockInDto attendance) {
        return new ResponseEntity<>(attendanceService.clockIn(attendance), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDto> getAttendanceById(@PathVariable("id") Long attendanceId) {
        return new ResponseEntity<>(attendanceService.getAttendanceById(attendanceId), HttpStatus.OK);
    }

    @PutMapping("/{id}/out")
    public ResponseEntity<AttendanceDto> clockOutAttendance(@PathVariable("id") Long attendanceId, @RequestBody @Valid ClockOutDto clockOut) {
        return new ResponseEntity<>(attendanceService.clockOut(attendanceId, clockOut), HttpStatus.OK);
    }

//    @DeleteMapping("/{id}")
//    public void deleteAttendance(@PathVariable Long id) {
//        attendanceService.deleteAttendance(id);
//    }
}
