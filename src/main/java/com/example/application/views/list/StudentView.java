package com.example.application.views.list;

import com.example.application.views.MainLayout;
import com.example.application.data.Student;
import com.example.application.services.StudentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.icon.*;
import jakarta.annotation.security.PermitAll;

import java.util.logging.Logger;

/*This class represents the view for managing students.
  It includes a grid for displaying student information,
  a filter for searching students by name, and a form for adding/editing student details.
*/

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Students | SMS")
public class StudentView extends VerticalLayout {

    // Logger for logging events
    private static final Logger LOGGER = Logger.getLogger(StudentView.class.getName());

    // Grid to display student information
    Grid<Student> grid = new Grid<>(Student.class);

    // TextField for filtering students by name
    TextField filterText = new TextField();

    // Form for adding/editing student details
    StudentForm form;

    // Service for interacting with student data
    StudentService service;

    // Constructor
    public StudentView(StudentService service) {
        this.service = service;

        // Set up the layout
        addClassName("list-view"); // Add CSS class to the layout
        setSizeFull(); // Set the layout size to full

        // Configure grid and form
        configureGrid();
        configureForm();

        // Configure the filter text field
        filterText.setSuffixComponent(new Icon(VaadinIcon.SEARCH)); // Add search icon to the text field

        // Add components to the layout
        add(getToolbar(), getContent()); // Add toolbar and content to the layout
        updateList(); // Update the grid with student data
        closeEditor(); // Close the editor form
    }

    // Method to get the content layout
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form); // Create a horizontal layout with grid and form
        content.setFlexGrow(2, grid); // Set grid to grow twice as much as the form
        content.setFlexGrow(1, form); // Set form to grow
        content.addClassNames("content"); // Add CSS class to the layout
        content.setSizeFull(); // Set the layout size to full
        return content; // Return the content layout
    }

    // Method to configure the student form
    private void configureForm() {
        form = new StudentForm(service.findAllCourses()); // Create a new student form with all courses
        form.setWidth("25em"); // Set the width of the form
        // Add listeners for save, delete, and close events
        form.addSaveListener(this::saveStudent);
        form.addDeleteListener(this::deleteStudent);
        form.addCloseListener(e -> closeEditor());
    }

    // Method to handle saving student
    private void saveStudent(StudentForm.SaveEvent event) {
        service.saveStudent(event.getStudent()); // Save the student using the service
        updateList(); // Update the grid with student data
        closeEditor(); // Close the editor form
    }

    // Method to handle deleting student
    private void deleteStudent(StudentForm.DeleteEvent event) {
        service.deleteStudent(event.getStudent()); // Delete the student using the service
        updateList(); // Update the grid with student data
        closeEditor(); // Close the editor form
    }

    // Method to configure the grid
    private void configureGrid() {
        grid.addClassNames("contact-grid"); // Add CSS class to the grid
        grid.setSizeFull(); // Set the grid size to full
        // Set columns to display in the grid
        grid.setColumns("studentNumber", "firstName", "lastName", "gender", "email");
        // Add a column to display the program name
        grid.addColumn(student -> student.getCourse().getName()).setHeader("Program");
        grid.getColumns().forEach(col -> col.setAutoWidth(true)); // Set columns to auto width

        // Listener for selecting a student in the grid to edit
        grid.asSingleSelect().addValueChangeListener(event ->
                editStudent(event.getValue())); // Edit the selected student
    }

    // Method to get the toolbar layout
    private HorizontalLayout getToolbar() {
        // Set up filter text field
        filterText.setPlaceholder("Search by name..."); // Set placeholder text
        filterText.addClassName("filter-field"); // Add CSS class to the text field
        filterText.setClearButtonVisible(true); // Show clear button
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // Set value change mode to lazy
        filterText.addValueChangeListener(e -> updateList()); // Update grid on value change

        // Button to add a new student
        Button addStudentButton = new Button("Add Student", new Icon(VaadinIcon.PLUS)); // Create a button with plus icon
        addStudentButton.addClickListener(click -> addStudent()); // Add listener to handle adding a student
        addStudentButton.addClassName("addstudent-button"); // Add CSS class to the button

        // Horizontal layout for the toolbar
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addStudentButton);
        toolbar.addClassName("toolbar"); // Add CSS class to the layout
        return toolbar; // Return the toolbar layout
    }

    // Method to edit a student
    public void editStudent(Student student) {
        if (student == null) {
            closeEditor(); // Close the editor if student is null
        } else {
            System.out.println("EDITING STUDENT: " + student.getFullName());
            form.setStudent(student); // Set the student in the form
            form.setVisible(true); // Show the form
            addClassName("editing"); // Add CSS class to indicate editing mode
        }
    }

    // Method to close the editor
    private void closeEditor() {
        form.setStudent(null); // Set form student to null
        form.setVisible(false); // Hide the form
        removeClassName("editing"); // Remove CSS class indicating editing mode
    }

    // Method to add a new student
    private void addStudent() {
        grid.asSingleSelect().clear(); // Clear selection in the grid
        editStudent(new Student()); // Edit a new student
    }

    // Method to update the grid with filtered students
    private void updateList() {
        grid.setItems(service.findAllStudents(filterText.getValue())); // Update grid with filtered students
    }
}
