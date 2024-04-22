package com.example.application.repository;

import com.example.application.data.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository interface provided by Spring Data JPA for CRUD operations on StudentCourse entities
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

}
