package com.aphatheology.attendancelogger.controller;


import com.aphatheology.attendancelogger.dto.AuthenticationResponse;
import com.aphatheology.attendancelogger.dto.LoginDto;
import com.aphatheology.attendancelogger.dto.StaffDto;
import com.aphatheology.attendancelogger.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid StaffDto registerBody, HttpServletRequest request) {
        return new ResponseEntity<>(authService.register(registerBody, request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginDto loginBody) {
        return new ResponseEntity<>(authService.login(loginBody), HttpStatus.CREATED);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyToken(@RequestParam("token") String token) {
        return new ResponseEntity<>(authService.verifyToken(token), HttpStatus.OK);
    }

    @GetMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationToken(@RequestParam("token") String oldToken, final HttpServletRequest request) {
        return new ResponseEntity<>(authService.resendVerificationToken(oldToken, request), HttpStatus.OK);
    }
}
