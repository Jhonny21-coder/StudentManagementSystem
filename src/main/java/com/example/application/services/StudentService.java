package com.example.application.services;

import com.example.application.data.Student;
import com.example.application.data.StudentCourse;
import com.example.application.repository.StudentCourseRepository;
import com.example.application.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private static StudentRepository studentRepository;
    private final StudentCourseRepository studentCourseRepository;

    public StudentService(StudentRepository studentRepository,
		StudentCourseRepository studentCourseRepository){
	this.studentRepository = studentRepository;
	this.studentCourseRepository = studentCourseRepository;
    }

    public static List<Student> getAllStudents(){
	return studentRepository.findAll();
    }

    public List<Student> findAllStudents(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return studentRepository.findAll();
        } else {
            return studentRepository.search(stringFilter);
        }
    }

    public long countStudents() {
        return studentRepository.count();
    }

    public void deleteStudent(Student student) {
        studentRepository.delete(student);
    }

    public void saveStudent(Student student) {
        if (student == null) {
            System.err.println("Student is null. Are you sure you have connected your form to the application?");
            return;
        }
        studentRepository.save(student);
    }

    public List<StudentCourse> findAllCourses() {
        return studentCourseRepository.findAll();
    }
}
