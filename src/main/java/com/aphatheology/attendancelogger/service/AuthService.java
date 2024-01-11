package com.aphatheology.attendancelogger.service;


import com.aphatheology.attendancelogger.dto.AuthenticationResponse;
import com.aphatheology.attendancelogger.dto.LoginDto;
import com.aphatheology.attendancelogger.dto.StaffDto;
import com.aphatheology.attendancelogger.entity.Role;
import com.aphatheology.attendancelogger.entity.StaffEntity;
import com.aphatheology.attendancelogger.entity.Tokens;
import com.aphatheology.attendancelogger.event.RegistrationCompleteEvent;
import com.aphatheology.attendancelogger.exception.ExistingEmailException;
import com.aphatheology.attendancelogger.repository.StaffRepository;
import com.aphatheology.attendancelogger.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final ApplicationEventPublisher publisher;

    public String getApplicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }

    public StaffDto map2Dto(StaffEntity staff) {
        StaffDto staffDto = new StaffDto();
        staffDto.setId(staff.getId());
        staffDto.setEmail(staff.getEmail());
        staffDto.setFullname(staff.getFullname());
        staffDto.setCreatedAt(staff.getCreatedAt());
        staffDto.setUpdatedAt(staff.getUpdatedAt());
        return staffDto;
    }

    public StaffEntity map2Entity(StaffDto staffDto) {
        StaffEntity staff = new StaffEntity();
        staff.setEmail(staffDto.getEmail());
        staff.setFullname(staffDto.getFullname());
        staff.setRole(Role.STAFF);
        staff.setPassword(this.passwordEncoder.encode(staffDto.getPassword()));

        return staff;
    }

    public AuthenticationResponse register(StaffDto registerBody, HttpServletRequest request) {
        Optional<StaffEntity> existingStaff = this.staffRepository.findStaffByEmail(registerBody.getEmail());

        if (existingStaff.isPresent()) throw new ExistingEmailException("Staff with this email already exist");

        StaffEntity staff = map2Entity(registerBody);
        this.staffRepository.save(staff);

        this.publisher.publishEvent(new RegistrationCompleteEvent(staff, getApplicationUrl(request)));

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", staff.getRole());
        extraClaims.put("id", staff.getId());

        return AuthenticationResponse.builder()
                .id(staff.getId())
                .email(staff.getEmail())
                .fullname(staff.getFullname())
                .role(staff.getRole())
                .token(this.jwtService.generateToken(extraClaims, staff))
                .build();
    }

    public AuthenticationResponse login(LoginDto loginBody) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginBody.getEmail(), loginBody.getPassword())
        );

        var staff = this.staffRepository.findStaffByEmail(loginBody.getEmail()).orElseThrow();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", staff.getRole());
        extraClaims.put("id", staff.getId());

        var jwtToken = this.jwtService.generateToken(extraClaims, staff);

        return AuthenticationResponse.builder()
                .id(staff.getId())
                .email(staff.getEmail())
                .fullname(staff.getFullname())
                .token(jwtToken)
                .build();
    }


    public void saveToken(StaffEntity staff, String token, String tokenType) {
        Tokens newToken = new Tokens(staff, token, tokenType);

        this.tokenRepository.save(newToken);
    }

    public String verifyToken(String token) {
        Tokens verificationToken = this.tokenRepository.findByTokenAndTokenType(token, "VERIFICATION");

        if(verificationToken == null) return "Invalid token";

        StaffEntity staff = verificationToken.getStaff();

        if(!Tokens.isValidToken(verificationToken.getExpirationTime())) {
            return "Expired token";
        }

        staff.setIsVerified(true);
        this.staffRepository.save(staff);
        this.tokenRepository.delete(verificationToken);

        return "Account verified successfully";

    }

    public String resendVerificationToken(String oldToken, HttpServletRequest request) {
        String token = this.generateNewToken(oldToken, "VERIFICATION");

        if(Objects.equals(token, "Invalid token")) return "Invalid Token";

        String url = this.getApplicationUrl(request) + "/auth/verify?token=" + token;

        log.info("Click the url to verify your account: " + url);
        return "Token resend successfully";
    }

    private String generateNewToken(String token, String tokenType) {
        Tokens findToken = this.tokenRepository.findByTokenAndTokenType(token, tokenType);
        if(findToken == null) return "Invalid token";

        findToken.updateToken(UUID.randomUUID().toString(), tokenType);
        Tokens updatedToken = tokenRepository.save(findToken);
        return updatedToken.getToken();
    }

}
