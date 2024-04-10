package com.example.application.data;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Column(name = "attendance_time")
    private LocalTime attendanceTime;

    @Convert(converter = AbsentStudentConverter.class)
    @Column(name = "absent_student_ids")
    private List<Long> absentStudentIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public LocalTime getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(LocalTime attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public List<Long> getAbsentStudentIds() {
	Collections.sort(absentStudentIds);
        return absentStudentIds;
    }

    public void setAbsentStudentIds(List<Long> absentStudentIds) {
        this.absentStudentIds = absentStudentIds;
    }

    public String getFormattedAttendanceTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        return attendanceTime.format(formatter);
    }
}
