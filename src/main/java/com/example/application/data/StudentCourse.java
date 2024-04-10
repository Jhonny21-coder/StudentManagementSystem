package com.example.application.data;

import org.hibernate.annotations.Formula;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;

@Entity
public class StudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "course")
    @Nullable
    private List<Student> students = new LinkedList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Formula("(select count(c.id) from student c where c.student_course_id = id)")
    private int studentsCount;

    public int getStudentsCount(){
        return studentsCount;
    }
}
