package com.example.application.views;

/*
  This class is the layout or view of the Attendance feature.
  This class is responsible for manipulating and managing student's attendance.
*/

import java.util.List;

import com.example.application.services.StudentService;
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
import com.vaadin.flow.router.PageTitle;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

// Route and title
@Route("attendance") // Route for accessing the view
@PageTitle("Attendance | SMS") // Title of the page
public class AttendanceView extends AppLayout { // Extends AppLayout for the application layout
    // Hibernate services for performing MySQL queries
    private final StudentService studentService; // Service for managing students
    private final AttendanceService attendanceService; // Service for managing attendance

    // Set services as part of the class
    public AttendanceView(StudentService studentService, AttendanceService attendanceService) {
        this.attendanceService = attendanceService; // Initialize attendance service
        this.studentService = studentService; // Initialize student service

        // Get all the list of existing students
        List<Student> students = studentService.getAllStudents();

        // Header of the layout
        Span header = new Span("Attendance");
        header.addClassName("header"); // Add CSS class to the header

        addClassName("nav"); // Add CSS class to the layout

        // Button invoking "showAttendanceList" method
        Button attendanceListButton = new Button("Attendance List", new Icon(VaadinIcon.LIST), event -> showAttendanceList());
        attendanceListButton.addClassName("attendance-button"); // Add CSS class to the button

        // Button for adding new attendance
        Button addAttendanceButton = new Button("Add New Attendance", new Icon(VaadinIcon.PLUS));
        addAttendanceButton.addClassName("add-button"); // Add CSS class to the button
        // Click listener to listen to the click event in this button
        addAttendanceButton.addClickListener(event -> {
            // Iterate through the list of students
            for(Student student : students){
                // Set status to true and save it to the database
                student.setStatus(false);
                studentService.saveStudent(student);
            }
            // Navigate to "AddAttendanceView" class
            getUI().ifPresent(ui -> ui.navigate(AddAttendanceView.class));
        });

        // Button for closing the current UI through invoking the root view
        Button closeButton = new Button("Close", new Icon(VaadinIcon.CLOSE), event -> getUI().ifPresent(ui -> ui.navigate("")));
        closeButton.addClassName("close-button"); // Add CSS class to the button

        // Place the created header and buttons in a horizontal form as header
        HorizontalLayout headerLayout = new HorizontalLayout(header, attendanceListButton, addAttendanceButton, closeButton);
        // Header and buttons spacing
        headerLayout.setSpacing(true);

        // Add header to navigation bar
        addToNavbar(headerLayout);

        // Grid component which represents the Student entity
        Grid<Student> grid = new Grid<>(Student.class, false);
        grid.setSizeFull();
        grid.addClassName("grid"); // Add CSS class to the grid

        // Grid column for numbering
        grid.addComponentColumn(student -> {
            Span indexSpan = new Span();
            int index = students.indexOf(student) + 1;

            indexSpan.setText(String.valueOf(index) + ".");

            return indexSpan;
        }).setHeader("No."); // Set header for the column

        // Other needed columns with auto width for flexibility
        grid.addColumn(Student::getStudentNumber).setHeader("Student Number").setAutoWidth(true); // Set header and width for the column
        grid.addColumn(Student::getId).setHeader("ID").setAutoWidth(true); // Set header and width for the column
        grid.addColumn(Student::getFullName).setHeader("Full Name").setAutoWidth(true); // Set header and width for the column
        grid.addColumn(Student::getAbsent).setHeader("Absences").setAutoWidth(true); // Set header and width for the column

        // Sort the list by students' last name
        Collections.sort(students, Comparator.comparing(Student::getLastName));

        // Set the sorted list to grid
        grid.setItems(students);

        // Set the grid as the main content of the layout
        setContent(grid);
    }

    // Method for displaying existing attendance
    private void showAttendanceList() {
        // Get all the list of existing attendance
        List<Attendance> pastAttendance = attendanceService.getAllAttendance();

        // Grid representing the Attendance entity
        Grid<Attendance> attendanceGrid = new Grid<>(Attendance.class, false);
        attendanceGrid.addClassName("grid"); // Add CSS class to the grid

        // Needed flexible grid columns
        attendanceGrid.addColumn(Attendance::getAttendanceDate).setHeader("Attendance Date").setAutoWidth(true); // Set header and width for the column
        attendanceGrid.addColumn(Attendance::getFormattedAttendanceTime).setHeader("Attendance Time").setAutoWidth(true); // Set header and width for the column
        attendanceGrid.addColumn(Attendance::getAbsentStudentIds).setHeader("ID of Absent Student/s").setAutoWidth(true); // Set header and width for the column

        // Set the list of attendance to grid
        attendanceGrid.setItems(pastAttendance);

        // Dialog displaying the list of existing attendance with header
        Span dialogHeader = new Span("Attendance List");

        Dialog dialog = new Dialog();
        dialog.addClassName("attendance-dialog"); // Add CSS class to the dialog
        dialog.getHeader().add(dialogHeader);
        dialog.setWidth("800px"); // Set width of the dialog

        // Add grid to dialog
        dialog.add(attendanceGrid);
        // Open dialog immediately
        dialog.open();
    }
}
