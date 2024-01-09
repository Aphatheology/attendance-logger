package com.aphatheology.attendancelogger.controller;


import com.aphatheology.attendancelogger.dto.AttendanceDto;
import com.aphatheology.attendancelogger.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AttendanceDto>> getAllAttendances() {
        return new ResponseEntity<>(attendanceService.getAllAttendances(), HttpStatus.OK);
    }

    @GetMapping("/day")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AttendanceDto>> getAllAttendancesByDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return new ResponseEntity<>(attendanceService.getAllAttendancesByDay(day), HttpStatus.OK);
    }

    @PostMapping("/in")
    public ResponseEntity<AttendanceDto> clockInAttendance(Principal principal) {
        return new ResponseEntity<>(attendanceService.clockIn(principal), HttpStatus.CREATED);
    }

    @PutMapping("/out")
    public ResponseEntity<AttendanceDto> clockOutAttendance(Principal principal) {
        return new ResponseEntity<>(attendanceService.clockOut(principal), HttpStatus.CREATED);
    }

}
