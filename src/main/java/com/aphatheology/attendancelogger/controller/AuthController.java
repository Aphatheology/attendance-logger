package com.aphatheology.attendancelogger.controller;


import com.aphatheology.attendancelogger.dto.AuthenticationResponse;
import com.aphatheology.attendancelogger.dto.LoginDto;
import com.aphatheology.attendancelogger.dto.StaffDto;
import com.aphatheology.attendancelogger.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid StaffDto registerBody) {
        return new ResponseEntity<>(authService.register(registerBody), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginDto loginBody) {
        return new ResponseEntity<>(authService.login(loginBody), HttpStatus.CREATED);
    }
}
