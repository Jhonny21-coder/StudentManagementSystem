package com.example.application.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Formula;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

// Annotation to mark this class as a JPA entity
@Entity
public class Student {

    // Primary key generated automatically by the database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Validation constraint: first name must not be empty
    @NotEmpty(message = "Please provide the first name")
    private String firstName = "";

    // Validation constraint: last name must not be empty
    @NotEmpty(message = "Please provide the last name")
    private String lastName = "";

    // Validation constraint: age must not be null
    @NotNull(message = "Please provide the age")
    private int age;

    // Many-to-one relationship with StudentCourse entity
    @ManyToOne
    @JoinColumn(name = "studentCourse_id")
    // Validation constraint: course must not be null
    @NotNull(message = "Please provide the course")
    // Ignore the 'students' property to prevent infinite recursion
    @JsonIgnoreProperties({"students"})
    private StudentCourse course;

    // Validation constraint: gender must not be empty
    @NotEmpty(message = "Please provide the gender")
    private String gender = "";

    // Validation constraint: email must be a valid email address and not empty
    @Email(message = "Please provide a valid email")
    @NotEmpty(message = "Please provide the email")
    private String email = "";

    // Validation constraint: student number must not be null
    @NotNull(message = "Please provide the student number")
    private long studentNumber;

    // Status of the student (absent/not absent)
    private boolean status;

    // Number of times student was absent
    private int absent;

    // Student grades fields for different subjects
    private double firstGrade;
    private double secondGrade;
    private double thirdGrade;
    private double fourthGrade;
    private double fifthGrade;
    private double sixthGrade;
    private double seventhGrade;
    private double eighthGrade;

    // Method to get the ID of the student
    public Long getId() {
    	return id;
    }

    // Method to set the ID of the student
    public void setId(Long id) {
    	this.id = id;
    }

    // Method to get the string representation of the student (used for printing)
    @Override
    public String toString() {
    	return firstName + " " + lastName;
    }

    // Method to get the first name of the student
    public String getFirstName() {
    	return firstName;
    }

    // Method to set the first name of the student
    public void setFirstName(String firstName) {
    	this.firstName = firstName;
    }

    // Method to get the last name of the student
    public String getLastName() {
    	return lastName;
    }

    // Method to set the last name of the student
    public void setLastName(String lastName) {
    	this.lastName = lastName;
    }

    // Method to get the age of the student
    public int getAge() {
    	return age;
    }

    // Method to set the age of the student
    public void setAge(int age) {
    	this.age = age;
    }

    // Method to get the course of the student
    public StudentCourse getCourse(){
    	return course;
    }

    // Method to set the course of the student
    public void setCourse(StudentCourse course){
    	this.course = course;
    }

    // Method to get the gender of the student
    public String getGender() {
    	return gender;
    }

    // Method to set the gender of the student
    public void setGender(String gender) {
    	this.gender = gender;
    }

    // Method to set the email of the student
    public void setEmail(String email){
    	this.email = email;
    }

    // Method to get the email of the student
    public String getEmail() {
    	return email;
    }

    // Method to get the student number
    public long getStudentNumber(){
    	return studentNumber;
    }

    // Method to set the student number
    public void setStudentNumber(long studentNumber){
    	this.studentNumber = studentNumber;
    }

    // Method to get the status of the student
    public boolean getStatus(){
    	return status;
    }

    // Method to set the status of the student
    public void setStatus(boolean status){
    	this.status = status;
    }

    // Method to get the number of absences of the student
    public int getAbsent(){
    	return absent;
    }

    // Method to set the number of absences of the student
    public void setAbsent(int absent){
    	this.absent = absent;
    }

    // Method to get the first grade of the student
    public double getFirstGrade(){
    	return firstGrade;
    }

    // Method to set the first grade of the student
    public void setFirstGrade(double firstGrade){
    	this.firstGrade = firstGrade;
    }

    // Method to get the second grade of the student
    public double getSecondGrade(){
    	return secondGrade;
    }

    // Method to set the second grade of the student
    public void setSecondGrade(double secondGrade){
    	this.secondGrade = secondGrade;
    }

    // Method to get the third grade of the student
    public double getThirdGrade(){
    	return thirdGrade;
    }

    // Method to set the third grade of the student
    public void setThirdGrade(double thirdGrade){
    	this.thirdGrade = thirdGrade;
    }

    // Method to get the fourth grade of the student
    public double getFourthGrade(){
    	return fourthGrade;
    }

    // Method to set the fourth grade of the student
    public void setFourthGrade(double fourthGrade){
    	this.fourthGrade = fourthGrade;
    }

    // Method to get the fifth grade of the student
    public double getFifthGrade(){
    	return fifthGrade;
    }

    // Method to set the fifth grade of the student
    public void setFifthGrade(double fifthGrade){
    	this.fifthGrade = fifthGrade;
    }

    // Method to get the sixth grade of the student
    public double getSixthGrade(){
    	return sixthGrade;
    }

    // Method to set the sixth grade of the student
    public void setSixthGrade(double sixthGrade){
    	this.sixthGrade = sixthGrade;
    }

    // Method to get the seventh grade of the student
    public double getSeventhGrade(){
    	return seventhGrade;
    }

    // Method to set the seventh grade of the student
    public void setSeventhGrade(double seventhGrade){
    	this.seventhGrade = seventhGrade;
    }

    // Method to get the eighth grade of the student
    public double getEigthGrade(){
    	return eighthGrade;
    }

    // Method to set the eighth grade of the student
    public void setEigthGrade(double eighthGrade){
    	this.eighthGrade = eighthGrade;
    }

    // Method to calculate and get the average grade of the student
    public String getAverage(){

    	// Array containing all the grades
    	double[] grades = {firstGrade, secondGrade, thirdGrade, fourthGrade,
        	fifthGrade, sixthGrade, seventhGrade, eighthGrade};

    	// Variable to store the total of all grades
    	double average = 0.0;

    	// Loop through all the grades
    	for(int i = 0; i < grades.length; i++){

            // Get the grade at the current index
            double totalGrades = grades[i];

            // Add each grade to the total
            average += totalGrades / grades.length;
   	}

        // Format the average to two decimal places
    	String finalAverage = String.format("%.2f", average);

    	// Return the formatted average
    	return finalAverage;
    }

    // Method to get the full name of the student
    public String getFullName(){
    	// Return concatenated first name and last name
    	return firstName + " " + lastName;
    }
}
