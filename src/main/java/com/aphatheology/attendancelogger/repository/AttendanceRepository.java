package com.aphatheology.attendancelogger.repository;

import com.aphatheology.attendancelogger.entity.AttendanceEntity;
import com.aphatheology.attendancelogger.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {
    List<AttendanceEntity> findAllByDay(LocalDate day);

    Optional<AttendanceEntity> findDistinctByStaffAndDay(StaffEntity staff, LocalDate day);
}
