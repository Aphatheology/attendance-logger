package com.aphatheology.attendancelogger.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClockInDto {
    @NotNull(message = "Staff Id cannot be null")
    private Long staffId;

    @NotNull(message = "day cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate day;

    @NotNull(message = "timeIn cannot be null")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeIn;
}
