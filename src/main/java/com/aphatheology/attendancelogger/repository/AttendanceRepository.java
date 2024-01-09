package com.aphatheology.attendancelogger.repository;

import com.aphatheology.attendancelogger.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {
    List<AttendanceEntity> findAllByDay(LocalDate day);
}
