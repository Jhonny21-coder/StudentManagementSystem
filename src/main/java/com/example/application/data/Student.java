package com.example.application.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Formula;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Please provide the first name")
    private String firstName = "";

    @NotEmpty(message = "Please provide the last name")
    private String lastName = "";

    @NotNull(message = "Please provide the age")
    private int age;

    @ManyToOne
    @JoinColumn(name = "studentCourse_id")
    @NotNull(message = "Please provide the course")
    @JsonIgnoreProperties({"students"})
    private StudentCourse course;

    @NotEmpty(message = "Please provide the gender")
    private String gender = "";

    @Email(message = "Please provide a valid email")
    @NotEmpty(message = "Please provide the email")
    private String email = "";

    @NotNull(message = "Please provide the student number")
    private long studentNumber;

    private boolean status;

    private int absent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public StudentCourse getCourse(){
	return course;
    }

    public void setCourse(StudentCourse course){
	this.course = course;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

   public void setEmail(String email){
	this.email = email;
   }

    public String getEmail() {
        return email;
    }

    public long getStudentNumber(){
	return studentNumber;
    }

    public void setStudentNumber(long studentNumber){
	this.studentNumber = studentNumber;
    }

    public boolean getStatus(){
	return status;
    }

    public void setStatus(boolean status){
	this.status = status;
    }

    public int getAbsent(){
	return absent;
    }

    public void setAbsent(int absent){
	this.absent = absent;
    }

    public String getFullName(){
	return firstName + " " + lastName;
    }
}
