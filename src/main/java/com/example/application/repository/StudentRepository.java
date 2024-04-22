package com.example.application.repository;

import com.example.application.data.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository interface provided by Spring Data JPA for CRUD operations on Student entities
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Custom query method using @Query annotation to define a JPQL query
    // This query searches for students whose first name or last name contains the specified search term
    // :searchTerm is a named parameter which can be passed in as an argument
    @Query("select c from Student c " +
            "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Student> search(@Param("searchTerm") String searchTerm);
}
