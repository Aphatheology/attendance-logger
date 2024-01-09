package com.aphatheology.attendancelogger.repository;

import com.aphatheology.attendancelogger.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, Long> {
    Optional<StaffEntity> findStaffByEmail(String email);

    Optional<StaffEntity> findByEmail(String email);
}
