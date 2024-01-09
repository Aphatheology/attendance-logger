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
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<AttendanceDto>> getAllAttendancesByDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return new ResponseEntity<>(attendanceService.getAllAttendancesByDay(day), HttpStatus.OK);
    }

    @PostMapping("/in")
    @PreAuthorize("#staffId == principal.id")
    public ResponseEntity<AttendanceDto> clockInAttendance(@RequestParam Long staffId) {
        return new ResponseEntity<>(attendanceService.clockIn(staffId), HttpStatus.CREATED);
    }

    @PutMapping("/out")
    public ResponseEntity<AttendanceDto> clockOutAttendance(Principal principal) {
        return new ResponseEntity<>(attendanceService.clockOut(principal), HttpStatus.CREATED);
    }

}
