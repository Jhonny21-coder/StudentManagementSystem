package com.example.application.repository;

import com.example.application.data.Attendance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository interface provided by Spring Data JPA for CRUD operations on Attendance entities
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

}
