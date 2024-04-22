package com.example.application.data;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

// Annotation to mark this class as a JPA entity
@Entity
public class Attendance {

    // Annotation to mark this field as the primary key
    @Id
    // Annotation to specify the generation strategy for the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Annotation to specify the mapping of this field to a column in the database
    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    // Annotation to specify the mapping of this field to a column in the database
    @Column(name = "attendance_time")
    private LocalTime attendanceTime;

    // Annotation to specify a custom converter for this field
    @Convert(converter = AbsentStudentConverter.class)
    // Annotation to specify the mapping of this field to a column in the database
    @Column(name = "absent_student_ids")
    private List<Long> absentStudentIds;

    // Getter method for the id field
    public Long getId() {
        return id; // Return the id
    }

    // Setter method for the id field
    public void setId(Long id) {
        this.id = id; // Set the id
    }

    // Getter method for the attendanceDate field
    public LocalDate getAttendanceDate() {
        return attendanceDate; // Return the attendance date
    }

    // Setter method for the attendanceDate field
    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate; // Set the attendance date
    }

    // Getter method for the attendanceTime field
    public LocalTime getAttendanceTime() {
        return attendanceTime; // Return the attendance time
    }

    // Setter method for the attendanceTime field
    public void setAttendanceTime(LocalTime attendanceTime) {
        this.attendanceTime = attendanceTime; // Set the attendance time
    }

    // Getter method for the absentStudentIds field
    public List<Long> getAbsentStudentIds() {
        // Sort the absent student ids before returning them
        Collections.sort(absentStudentIds);
        return absentStudentIds; // Return the absent student ids
    }

    // Setter method for the absentStudentIds field
    public void setAbsentStudentIds(List<Long> absentStudentIds) {
        this.absentStudentIds = absentStudentIds; // Set the absent student ids
    }

    // Method to get the formatted attendance time as a string
    public String getFormattedAttendanceTime() {
        // Define a DateTimeFormatter for formatting LocalTime to a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        // Format the attendance time using the formatter
        return attendanceTime.format(formatter); // Return the formatted attendance time
    }
}
