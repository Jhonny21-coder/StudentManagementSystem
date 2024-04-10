package com.example.application.repository;

import com.example.application.data.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

	@Query("select c from Student c " +
        "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
        "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
      List<Student> search(@Param("searchTerm") String searchTerm);
}
