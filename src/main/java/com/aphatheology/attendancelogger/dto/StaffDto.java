package com.aphatheology.attendancelogger.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {
    private Long id;

    @NotBlank(message = "Email cannot be blank")
    @Email(regexp = ".+[@].+[\\.].+", message = "Invalid email")
    private String email;

    @NotBlank(message = "Fullname cannot be blank")
    @NotNull
    private String fullname;

    @Size(min = 6, max = 20, message = "Invalid password, password must be between 6 to 20 characters")
    @NotBlank(message = "Password can not be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,20}$", message = "Invalid password, it must have at least one lower case, upper case and number")
    private String password;

    private String role;
    private Boolean verified;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
