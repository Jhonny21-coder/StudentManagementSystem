package com.example.application.data;

import org.hibernate.annotations.Formula;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;

// Annotation to mark this class as a JPA entity
@Entity
public class StudentCourse {

    // Annotation to mark this field as the primary key
    @Id
    // Annotation to specify the generation strategy for the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Annotation to specify validation constraints for this field (must not be blank)
    @NotBlank
    private String name;

    // Annotation to specify the mapping of this field to a one-to-many relationship in the database
    @OneToMany(mappedBy = "course")
    // Annotation to indicate that the field can be null
    @Nullable
    private List<Student> students = new LinkedList<>();

    // Getter method for the id field
    public Long getId() {
        return id; // Return the id
    }

    // Setter method for the id field
    public void setId(Long id) {
        this.id = id; // Set the id
    }

    // Getter method for the name field
    public String getName() {
        return name; // Return the name
    }

    // Setter method for the name field
    public void setName(String name) {
        this.name = name; // Set the name
    }

    // Getter method for the students field
    public List<Student> getStudents() {
        return students; // Return the list of students
    }

    // Setter method for the students field
    public void setStudents(List<Student> students) {
        this.students = students; // Set the list of students
    }

    // Formula annotation to define a SQL expression for calculating a property
    @Formula("(select count(c.id) from student c where c.student_course_id = id)")
    // Field to hold the result of the SQL expression
    private int studentsCount;

    // Getter method for the studentsCount field
    public int getStudentsCount(){
        return studentsCount; // Return the count of students
    }
}
