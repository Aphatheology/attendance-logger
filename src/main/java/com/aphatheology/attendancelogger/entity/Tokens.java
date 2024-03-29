package com.aphatheology.attendancelogger.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Data
@NoArgsConstructor
public class Tokens {
    private static final int EXPIRATION_TIME = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expirationTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "staff_id", nullable = false, foreignKey = @ForeignKey(name = "FK_TOKEN_STAFF"))
    private StaffEntity staff;

    private String tokenType;

    public Tokens(StaffEntity staff, String token, String tokenType) {
        super();

        this.staff = staff;
        this.token = token;
        this.tokenType = tokenType;
        this.expirationTime = calculateExpirationTime();
    }

    public Tokens(String token) {
        super();
        this.token = token;
        this.expirationTime = calculateExpirationTime();
    }

    private LocalDateTime calculateExpirationTime() {
        return LocalDateTime.now().plusMinutes(Tokens.EXPIRATION_TIME);
    }

    public static boolean isValidToken(LocalDateTime expiryDate) {
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), expiryDate);
        return minutes >= 0;
    }

    public void updateToken(String code, String tokenType){
        this.token = code;
        this.tokenType = tokenType;
        this.expirationTime = calculateExpirationTime();
    }
}
