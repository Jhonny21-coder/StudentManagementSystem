package com.example.application.views;

/*
  This class is the layout or view of the Grades feature.
  This class is responsible for displaying student's grades.
*/

import java.util.List;

import com.example.application.services.AttendanceService;
import com.example.application.data.Attendance;
import com.example.application.data.Student;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.Route;
import com.example.application.services.StudentService;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.router.PageTitle;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

/*@Route("grades")
@PageTitle("Grades | SMS")
public class GradesView extends AppLayout {
    private final StudentService studentService;

    private final TextField firstField = new TextField("First Grade");
    private final TextField secondField = new TextField("Second Grade");
    private final TextField thirdField = new TextField("Third Grade");
    private final TextField fourthField = new TextField("Fouth Grade");
    private final TextField fifthField = new TextField("Fifth Grade");
    private final TextField sixthField = new TextField("Sixth Grade");
    private final TextField seventhField = new TextField("Seventh Grade");
    private final TextField eighthField = new TextField("Eighth Grade");

    public GradesView(StudentService studentService) {
        this.studentService = studentService;

        List<Student> students = studentService.getAllStudents();

        Span header = new Span("Grades");
        header.addClassName("grade-header");

        addClassName("nav");

        Button closeButton = new Button("Close", new Icon(VaadinIcon.CLOSE), event -> getUI().ifPresent(ui -> ui.navigate("")));
        closeButton.addClassName("grade-close-button");

	Button viewButton = new Button("View Top 10 Students", new Icon(VaadinIcon.EYE));
	viewButton.addClickListener(event -> {
	    viewTopStudents();
	});
	viewButton.addClassName("view-button");

        HorizontalLayout headerLayout = new HorizontalLayout(header, viewButton, closeButton);
        headerLayout.setSpacing(true);

        addToNavbar(headerLayout);

        Grid<Student> grid = new Grid<>(Student.class, false);
        grid.setSizeFull();
        grid.addClassName("grid");

        grid.addComponentColumn(student -> {
            Span indexSpan = new Span();
            int index = students.indexOf(student) + 1;

            indexSpan.setText(String.valueOf(index) + ".");

            return indexSpan;
        }).setHeader("No.");

        grid.addColumn(Student::getFullName).setHeader("Full Name").setAutoWidth(true);
	grid.addComponentColumn(student -> {
	     Span averageSpan = new Span(student.getAverage() + "%");

	     return averageSpan;
	}).setHeader("Average");

        grid.addColumn(createStatusComponentRenderer()).setHeader("Remarks");

	grid.addComponentColumn(student -> {
	     Icon editIcon = new Icon(VaadinIcon.PENCIL);
	     editIcon.addClickListener(event -> {
		createGradesForm(student, grid);
	     });

	     return editIcon;
	}).setHeader("Edit Grade");

        Collections.sort(students, Comparator.comparing(Student::getLastName));

        grid.setItems(students);

        setContent(grid);
    }

    private static final SerializableBiConsumer<Span, Student> statusComponentUpdater = (
    		span, student) -> {

        double average = Double.parseDouble(student.getAverage());

        String theme = "";
	String remarks = "";

        if(average >= 75){
          theme = "badge success";
          remarks = "PASSED";
        }else if(average == 0){
           theme = "badge";
           remarks = "NONE";
        }else{
           theme = "badge error";
           remarks = "FAILED";
        }

        span.getElement().setAttribute("theme", theme);
        span.setText(remarks);

    };

    private static ComponentRenderer<Span, Student> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    private void viewTopStudents(){

    	List<Student> students = studentService.getAllStudents();

    	Dialog dialog = new Dialog();
	dialog.addClassName("attendance-dialog");

    	Span header = new Span("TOP 10 STUDENTS");
    	dialog.getHeader().add(header);

    	Collections.sort(students, Comparator.comparing(Student::getAverage).reversed());

    	VerticalLayout topLayout = new VerticalLayout();

    	for(int i = 0; i < Math.min(10, students.size()); i++){
            Student student = students.get(i);
            Span average = new Span(String.valueOf(student.getAverage() + "%"));
            average.getStyle().set("color","green");

            Span span = new Span("Top " + (i + 1) + ". " + String.valueOf(student.getFullName() + ","));

            HorizontalLayout containerLayout = new HorizontalLayout(span, average);
            topLayout.add(containerLayout);
    	}

    	dialog.add(topLayout);
    	dialog.open();
    }

    private void createGradesForm(Student student, Grid<Student> grid){
	setFormValues(student);

	FormLayout form = new FormLayout();
	form.add(firstField, secondField);
	form.add(thirdField, fourthField);
	form.add(fifthField, sixthField);
	form.add(seventhField, eighthField);

	ConfirmDialog dialog = new ConfirmDialog();
	dialog.setCancelable(true);
        dialog.setConfirmText("Save");
        dialog.setConfirmButtonTheme("primary");
	dialog.setWidth("800px");

	String fullName = "";
	if(student.getFullName().toLowerCase().endsWith("s")){
	   fullName = student.getFullName() + "' Grades";
	}else{
	   fullName = student.getFullName() + "'s Grades";
	}

	dialog.setHeader(fullName);
	dialog.add(form);
	dialog.open();
	dialog.addConfirmListener(event -> {
	    String firstValue = firstField.getValue();
	    String secondValue = secondField.getValue();
	    String thirdValue = thirdField.getValue();
	    String fourthValue = fourthField.getValue();
	    String fifthValue = fifthField.getValue();
	    String sixthValue = sixthField.getValue();
	    String seventhValue = seventhField.getValue();
	    String eighthValue = eighthField.getValue();

	    student.setFirstGrade(Double.parseDouble(firstValue));
	    student.setSecondGrade(Double.parseDouble(secondValue));
	    student.setThirdGrade(Double.parseDouble(thirdValue));
	    student.setFourthGrade(Double.parseDouble(fourthValue));
	    student.setFifthGrade(Double.parseDouble(fifthValue));
	    student.setSixthGrade(Double.parseDouble(sixthValue));
	    student.setSeventhGrade(Double.parseDouble(seventhValue));
	    student.setEigthGrade(Double.parseDouble(eighthValue));

	    studentService.saveStudent(student);

            grid.getDataProvider().refreshItem(student);

	});

	dialog.addClassName("grades-dialog");
    }

    public void setFormValues(Student student){
	firstField.setValue(String.valueOf(student.getFirstGrade()));
        secondField.setValue(String.valueOf(student.getSecondGrade()));
        thirdField.setValue(String.valueOf(student.getThirdGrade()));
        fourthField.setValue(String.valueOf(student.getFourthGrade()));
        fifthField.setValue(String.valueOf(student.getFifthGrade()));
        sixthField.setValue(String.valueOf(student.getSixthGrade()));
        seventhField.setValue(String.valueOf(student.getSeventhGrade()));
        eighthField.setValue(String.valueOf(student.getEigthGrade()));

	firstField.addClassName("field");
        secondField.addClassName("field");
        thirdField.addClassName("field");
        fourthField.addClassName("field");
        fifthField.addClassName("field");
        sixthField.addClassName("field");
        seventhField.addClassName("field");
        eighthField.addClassName("field");
    }
}*/

// Route and title
@Route("grades") // Route for accessing the view
@PageTitle("Grades | SMS") // Title of the page
public class GradesView extends AppLayout {

    // Service for managing students
    private final StudentService studentService;

    // TextFields for each grade input
    private final TextField firstField = new TextField("First Grade"); // TextField for the first grade
    private final TextField secondField = new TextField("Second Grade"); // TextField for the second grade
    private final TextField thirdField = new TextField("Third Grade"); // TextField for the third grade
    private final TextField fourthField = new TextField("Fourth Grade"); // TextField for the fourth grade
    private final TextField fifthField = new TextField("Fifth Grade"); // TextField for the fifth grade
    private final TextField sixthField = new TextField("Sixth Grade"); // TextField for the sixth grade
    private final TextField seventhField = new TextField("Seventh Grade"); // TextField for the seventh grade
    private final TextField eighthField = new TextField("Eighth Grade"); // TextField for the eighth grade

    // Constructor initializing the view with a StudentService
    public GradesView(StudentService studentService) {
        this.studentService = studentService; // Initialize student service

        // Fetching all students
        List<Student> students = studentService.getAllStudents();

        // Creating and styling header
        Span header = new Span("Grades"); // Header text
        header.addClassName("grade-header"); // Add CSS class to the header

        // Adding CSS class to the layout
        addClassName("nav");

        // Creating Close button
        Button closeButton = new Button("Close", new Icon(VaadinIcon.CLOSE), event -> getUI().ifPresent(ui -> ui.navigate(""))); // Close button
        closeButton.addClassName("grade-close-button"); // Add CSS class to the button

        // Creating View Top 10 Students button
        Button viewButton = new Button("View Top 10 Students", new Icon(VaadinIcon.EYE)); // View Top 10 Students button
        viewButton.addClickListener(event -> {
            viewTopStudents(); // Action to view top 10 students
        });
        viewButton.addClassName("view-button"); // Add CSS class to the button

        // Creating header layout with buttons
        HorizontalLayout headerLayout = new HorizontalLayout(header, viewButton, closeButton); // Layout for header and buttons
        headerLayout.setSpacing(true); // Add spacing between components

        // Adding header layout to the navigation bar
        addToNavbar(headerLayout);

        // Creating a grid to display student data
        Grid<Student> grid = new Grid<>(Student.class, false); // Grid for displaying student data
        grid.setSizeFull(); // Set grid size to full
        grid.addClassName("grid"); // Add CSS class to the grid

        // Adding column for student index
        grid.addComponentColumn(student -> {
            Span indexSpan = new Span(); // Span element for index
            int index = students.indexOf(student) + 1; // Get index of student
            indexSpan.setText(String.valueOf(index) + "."); // Set index text
            return indexSpan; // Return index span
        }).setHeader("No."); // Set header for the column

        // Adding column for student full name
        grid.addColumn(Student::getFullName).setHeader("Full Name").setAutoWidth(true); // Column for student full name

        // Adding column for student average grade
        grid.addComponentColumn(student -> {
            Span averageSpan = new Span(student.getAverage() + "%"); // Span for average grade
            return averageSpan; // Return average span
        }).setHeader("Average"); // Set header for the column

        // Adding column for student remarks
        grid.addColumn(createStatusComponentRenderer()).setHeader("Remarks"); // Column for student remarks

        // Adding column for editing student grades
        grid.addComponentColumn(student -> {
            Icon editIcon = new Icon(VaadinIcon.PENCIL); // Icon for editing grade
            editIcon.addClickListener(event -> {
                createGradesForm(student, grid); // Action to create grade form
            });
            return editIcon; // Return edit icon
        }).setHeader("Edit Grade"); // Set header for the column

        // Sorting students by last name
        Collections.sort(students, Comparator.comparing(Student::getLastName)); // Sort students by last name

        // Setting items for the grid
        grid.setItems(students); // Set students as grid items

        // Setting grid as the content of the view
        setContent(grid); // Set grid as view content
    }

    // Create a BiConsumer for updating students' status
    private static final SerializableBiConsumer<Span, Student> statusComponentUpdater = (
                span, student) -> {

	// Getting student average grade
        double average = Double.parseDouble(student.getAverage());

	// Empty variables for storing status theme and remarks
        String theme = "";
        String remarks = "";

	// Checks if average is above or equal to 75
        if(average >= 75){
          // If so, set theme to success and badge to passed
          theme = "badge success";
          remarks = "PASSED";
        // Checks if average is equal to 0
        }else if(average == 0){
           // If equal to 0, set theme to default and remarks to none
           theme = "badge";
           remarks = "NONE";
        // Checks if average is below 75 and not equal to 0
        }else{
           // If so, set theme to error and remarks to failed
           theme = "badge error";
           remarks = "FAILED";
        }

	// Set theme attribute
        span.getElement().setAttribute("theme", theme);
        // Set remarks
        span.setText(remarks);

    };

    // Creates the renderer for updating students' status
    private static ComponentRenderer<Span, Student> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater); // return rendered status
    }

    // Method to display top students
    private void viewTopStudents(){
	// Get all list of students
        List<Student> students = studentService.getAllStudents();

        // Create dialog for displaying top students
        Dialog dialog = new Dialog();
        dialog.addClassName("attendance-dialog"); // Add CSS class to dialog

	// Create the header for dialog
        Span header = new Span("TOP 10 STUDENTS");
        dialog.getHeader().add(header); // Set the header to dialog

	// Sort students by thier average in descending order
        Collections.sort(students, Comparator.comparing(Student::getAverage).reversed());

	// Vertical layout for holding components
        VerticalLayout topLayout = new VerticalLayout();

	// Iterate through the list of students and get 10 students only
        for(int i = 0; i < Math.min(10, students.size()); i++){
            // Get 10 student one by one
            Student student = students.get(i);

            // Display student's average
            Span average = new Span(String.valueOf(student.getAverage() + "%"));
            average.getStyle().set("color","green"); // Add CSS style to average

	    // Display student's full name
            Span topFullName = new Span("Top " + (i + 1) + ". " + String.valueOf(student.getFullName() + ","));

	    // Arrange average and full name horizontally
            HorizontalLayout containerLayout = new HorizontalLayout(topFullName, average);
            topLayout.add(containerLayout); // Add arranged average and full name to top layout
        }

	// Add top layout to dialog
        dialog.add(topLayout);
        // Open dialog immediately
        dialog.open();
    }

    // Creates the form for grades
    private void createGradesForm(Student student, Grid<Student> grid){
    	// Set student's grades to the form
        setFormValues(student);

	// Create rows for displaying student's grades
        FormLayout form = new FormLayout();
        form.add(firstField, secondField);
        form.add(thirdField, fourthField);
        form.add(fifthField, sixthField);
        form.add(seventhField, eighthField);

	// Create dialog for saving or closing the form
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setCancelable(true); // Set dialog to cancellable
        dialog.setConfirmText("Save"); // Add save button to dialog
        dialog.setConfirmButtonTheme("primary"); // Set save button theme to primary
        dialog.setWidth("800px"); // Set dialog width

        String fullName = "";
        // Checks if student's full name ends with s
        if(student.getFullName().toLowerCase().endsWith("s")){
           // If it ends with s, add ' to student's full name
           fullName = student.getFullName() + "' Grades";
        }else{
	   // If student's full name not ends with s, add s to student's full name
           fullName = student.getFullName() + "'s Grades";
        }

        // Set student's full name as dialog's header
        dialog.setHeader(fullName);
        // Add form to dialog
        dialog.add(form);
        // Open dialog immediately
        dialog.open();
        // Save button listener for setting updated grades to form and saving to databasea
        dialog.addConfirmListener(event -> {
            String firstValue = firstField.getValue();
            String secondValue = secondField.getValue();
            String thirdValue = thirdField.getValue();
            String fourthValue = fourthField.getValue();
            String fifthValue = fifthField.getValue();
            String sixthValue = sixthField.getValue();
            String seventhValue = seventhField.getValue();
            String eighthValue = eighthField.getValue();

            student.setFirstGrade(Double.parseDouble(firstValue));
            student.setSecondGrade(Double.parseDouble(secondValue));
            student.setThirdGrade(Double.parseDouble(thirdValue));
            student.setFourthGrade(Double.parseDouble(fourthValue));
            student.setFifthGrade(Double.parseDouble(fifthValue));
            student.setSixthGrade(Double.parseDouble(sixthValue));
            student.setSeventhGrade(Double.parseDouble(seventhValue));
            student.setEigthGrade(Double.parseDouble(eighthValue));

	    // Save updated grades to database
            studentService.saveStudent(student);
            // Refresh the grid
            grid.getDataProvider().refreshItem(student);

        });

        dialog.addClassName("grades-dialog"); // Add CSS class to dialog
    }

    // Method to populate form with student's grades
    public void setFormValues(Student student){
        firstField.setValue(String.valueOf(student.getFirstGrade()));
        secondField.setValue(String.valueOf(student.getSecondGrade()));
        thirdField.setValue(String.valueOf(student.getThirdGrade()));
        fourthField.setValue(String.valueOf(student.getFourthGrade()));
        fifthField.setValue(String.valueOf(student.getFifthGrade()));
        sixthField.setValue(String.valueOf(student.getSixthGrade()));
        seventhField.setValue(String.valueOf(student.getSeventhGrade()));
        eighthField.setValue(String.valueOf(student.getEigthGrade()));

	// Adding CSS classes to the grades' fields
        firstField.addClassName("field");
        secondField.addClassName("field");
        thirdField.addClassName("field");
        fourthField.addClassName("field");
        fifthField.addClassName("field");
        sixthField.addClassName("field");
        seventhField.addClassName("field");
        eighthField.addClassName("field");
    }
}
