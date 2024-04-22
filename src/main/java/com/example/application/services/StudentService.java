package com.example.application.services;

import com.example.application.data.Student;
import com.example.application.data.StudentCourse;
import com.example.application.repository.StudentCourseRepository;
import com.example.application.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Service class responsible for handling business logic related to Student entities
@Service
public class StudentService {

    // Reference to the StudentRepository for database operations related to students
    private final StudentRepository studentRepository;

    // Reference to the StudentCourseRepository for database operations related to student courses
    private final StudentCourseRepository studentCourseRepository;

    // Constructor injection to initialize the repositories
    public StudentService(StudentRepository studentRepository, StudentCourseRepository studentCourseRepository){
        this.studentRepository = studentRepository; // Initialize StudentRepository
        this.studentCourseRepository = studentCourseRepository; // Initialize StudentCourseRepository
    }

    // Method to retrieve all students from the database
    public List<Student> getAllStudents(){
        return studentRepository.findAll(); // Return all students from repository
    }

    // Method to find students based on a search filter
    public List<Student> findAllStudents(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return studentRepository.findAll(); // Return all students if no filter is provided
        } else {
            return studentRepository.search(stringFilter); // Return filtered students
        }
    }

    // Method to count the total number of students in the database
    public long countStudents() {
        return studentRepository.count(); // Return the total count of students
    }

    // Method to delete a student from the database
    public void deleteStudent(Student student) {
        studentRepository.delete(student); // Delete student from the repository
    }

    // Method to save a student to the database
    public void saveStudent(Student student) {
        // Check if the student object is null before saving
        if (student == null) {
            System.err.println("Student is null. Are you sure you have connected your form to the application?");
            return;
        }
        studentRepository.save(student); // Save student to the repository
    }

    // Method to retrieve all student courses from the database
    public List<StudentCourse> findAllCourses() {
        return studentCourseRepository.findAll(); // Return all student courses from repository
    }
}
