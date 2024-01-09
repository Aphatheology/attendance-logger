package com.aphatheology.attendancelogger.service;


import com.aphatheology.attendancelogger.dto.AuthenticationResponse;
import com.aphatheology.attendancelogger.dto.LoginDto;
import com.aphatheology.attendancelogger.dto.StaffDto;
import com.aphatheology.attendancelogger.entity.Role;
import com.aphatheology.attendancelogger.entity.StaffEntity;
import com.aphatheology.attendancelogger.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public StaffDto map2Dto(StaffEntity staff) {
        StaffDto staffDto = new StaffDto();
        staffDto.setId(staff.getId());
        staffDto.setEmail(staff.getEmail());
        staffDto.setFullname(staff.getFullname());
        staffDto.setCreatedAt(staff.getCreatedAt());
        staffDto.setUpdatedAt(staff.getUpdatedAt());
        return staffDto;
    }

    public StaffEntity map2Entity(StaffDto userDto) {
        StaffEntity staff = new StaffEntity();
        staff.setEmail(userDto.getEmail());
        staff.setFullname(userDto.getFullname());
        staff.setRole(Role.STAFF);
        staff.setPassword(this.passwordEncoder.encode(userDto.getPassword()));

        return staff;
    }

    public AuthenticationResponse register(StaffDto registerBody) {
        StaffEntity staff = map2Entity(registerBody);
        this.staffRepository.save(staff);

        return AuthenticationResponse.builder()
                .id(staff.getId())
                .email(staff.getEmail())
                .fullname(staff.getFullname())
                .token(this.jwtService.generateToken(staff))
                .build();
    }

    public AuthenticationResponse login(LoginDto loginBody) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginBody.getEmail(), loginBody.getPassword())
        );

        var staff = this.staffRepository.findStaffByEmail(loginBody.getEmail()).orElseThrow();

        var jwtToken = this.jwtService.generateToken(staff);

        return AuthenticationResponse.builder()
                .id(staff.getId())
                .email(staff.getEmail())
                .fullname(staff.getFullname())
                .token(jwtToken)
                .build();
    }
}
