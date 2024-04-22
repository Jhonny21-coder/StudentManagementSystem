package com.example.application.services;

import com.example.application.data.Attendance;
import com.example.application.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Service class responsible for handling business logic related to Attendance entities
@Service
public class AttendanceService {

    // Reference to the AttendanceRepository for database operations
    private static AttendanceRepository attendanceRepository;

    // Constructor injection to initialize the AttendanceRepository
    public AttendanceService(AttendanceRepository attendanceRepository){
        this.attendanceRepository = attendanceRepository; // Initialize AttendanceRepository
    }

    // Method to save attendance data to the database
    public void saveAttendance(Attendance attendance){
        attendanceRepository.save(attendance); // Save attendance data to the repository
    }

    // Method to retrieve all attendance records from the database
    public List<Attendance> getAllAttendance(){
        return attendanceRepository.findAll(); // Return all attendance records from repository
    }
}
