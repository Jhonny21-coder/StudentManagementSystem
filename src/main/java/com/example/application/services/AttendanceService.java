package com.example.application.services;

import com.example.application.data.Attendance;
import com.example.application.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    private static AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository){
        this.attendanceRepository = attendanceRepository;
    }

    public void saveAttendance(Attendance attendance){
	attendanceRepository.save(attendance);
    }

    public List<Attendance> getAllAttendance(){
	return attendanceRepository.findAll();
    }

    /*public List<Attendance> findAttendanceByAbsentStudentId(Long studentId) {
        return attendanceRepository.findByAbsentStudentIdsIn(studentId);
    }*/
}
