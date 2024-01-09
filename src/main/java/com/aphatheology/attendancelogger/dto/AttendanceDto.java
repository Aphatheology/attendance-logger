package com.aphatheology.attendancelogger.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDto {

    private Long id;

    @NotBlank(message = "Staff Id cannot be blank")
    private Long staffId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate day;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeIn;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeOut;
}
