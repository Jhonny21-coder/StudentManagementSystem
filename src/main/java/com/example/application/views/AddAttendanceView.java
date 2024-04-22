package com.example.application.views;

/*
  This class is the layout or view of the Add Attendance feature.
  This class is responsible for adding student's attendance.
*/

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

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

// Route and title
@Route("addAttendance") // Route for accessing the view
@PageTitle("AddAttendance | SMS") // Title of the page
public class AddAttendanceView extends AppLayout {

    // Services for managing students and attendance
    private final StudentService studentService; // Service for managing students
    private final AttendanceService attendanceService; // Service for managing attendance

    // Constructor
    public AddAttendanceView(StudentService studentService, AttendanceService attendanceService) {
        this.attendanceService = attendanceService; // Initialize attendance service
        this.studentService = studentService; // Initialize student service

        // Header components
        Span date = new Span("[ " + String.valueOf(LocalDate.now()) + " ]"); // Span for displaying the current date
        date.addClassName("date"); // Add CSS class to the date span
        Span header = new Span("Attendance "); // Span for the header
        header.addClassName("add-header"); // Add CSS class to the header span

        addClassName("nav"); // Add CSS class to the layout

        // Get all students and initialize a list to store absent student IDs
        List<Student> students = studentService.getAllStudents(); // Get all students
        List<Long> idList = new ArrayList<>(); // List to store absent student IDs

        // Buttons for closing and saving attendance
        Button closeButton = new Button("Close", new Icon(VaadinIcon.CLOSE), event -> getUI().ifPresent(ui -> ui.navigate(AttendanceView.class))); // Close button
        closeButton.addClassName("close-button"); // Add CSS class to the close button

        Button saveButton = new Button("Save", new Icon(VaadinIcon.CHECK)); // Save button
        saveButton.addClassName("save-button"); // Add CSS class to the save button
        saveButton.addClickListener(event -> {
            // Iterate through students list
            for (Student student : students) {
                if (!student.getStatus()) { // Check if student is absent
                    idList.add(student.getId()); // Add absent student ID to the list
                    student.setAbsent(student.getAbsent() + 1); // Increment absent count
                    studentService.saveStudent(student); // Save student
                }
            }

            ZoneId zoneId = ZoneId.of("America/New_York"); // Set time zone

            // Get the current time in the specified time zone
            LocalTime localTime = LocalTime.now(zoneId);

            Attendance attendance = new Attendance(); // Create new attendance object
            attendance.setAttendanceDate(LocalDate.now()); // Set attendance date
            attendance.setAttendanceTime(localTime); // Set attendance time
            attendance.setAbsentStudentIds(idList); // Set absent student IDs
            attendanceService.saveAttendance(attendance); // Save attendance
            getUI().ifPresent(ui -> ui.navigate(AttendanceView.class)); // Navigate back to attendance view
        });

        // Horizontal layout for header components
        HorizontalLayout headerLayout = new HorizontalLayout(
                header, date, saveButton, closeButton); // Header components
        headerLayout.setSpacing(true); // Set spacing between components

        addToNavbar(headerLayout); // Add header layout to the navigation bar

        // Grid component for displaying students
        Grid<Student> grid = new Grid<>(Student.class, false); // Create grid with Student class as data type
        grid.setSizeFull(); // Set grid size to full
        grid.addClassName("grid"); // Add CSS class to the grid

        // Column for numbering
        grid.addComponentColumn(student -> {
            Span indexSpan = new Span(); // Span for displaying index
            int index = students.indexOf(student) + 1; // Calculate index

            indexSpan.setText(String.valueOf(index) + "."); // Set index text

            return indexSpan; // Return index span
        }).setHeader("No."); // Set header for the column

        // Column for marking attendance
        grid.addComponentColumn(student -> {
            Checkbox checkbox = new Checkbox(); // Checkbox for marking attendance

            checkbox.setValue(student.getStatus()); // Set checkbox value
            checkbox.addValueChangeListener(event -> {
                boolean newStatus = event.getValue(); // Get new status from checkbox value change

                student.setStatus(newStatus); // Update student status
                studentService.saveStudent(student); // Save student

                // Update the badge color live
                grid.getDataProvider().refreshItem(student); // Refresh grid
            });
            return checkbox; // Return checkbox
        }).setHeader("Mark as Present"); // Set header for the column

        // Other columns
        grid.addColumn(Student::getId).setHeader("ID").setAutoWidth(true); // ID column
        grid.addColumn(Student::getFullName).setHeader("Full Name").setAutoWidth(true); // Full name column
        grid.addColumn(createStatusComponentRenderer()).setHeader("Is Present?"); // Is present column

        // Sort the list by students' last name
        Collections.sort(students, Comparator.comparing(Student::getLastName)); // Sort students by last name

        grid.setItems(students); // Set student data to grid

        setContent(grid); // Set grid as main content of the layout
    }

    // Method for creating status component renderer
    private static final SerializableBiConsumer<Span, Student> statusComponentUpdater = (
            span, student) -> {
        boolean isAvailable = student.getStatus(); // Get student status
        String theme = String.format("badge %s", isAvailable ? "success" : "error"); // Determine badge theme based on status
        span.getElement().setAttribute("theme", theme); // Set theme attribute for span element

        span.setText(String.valueOf(student.getStatus())); // Set text for span element
    };

    // Method for creating status component renderer
    private static ComponentRenderer<Span, Student> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater); // Create component renderer
    }
}
