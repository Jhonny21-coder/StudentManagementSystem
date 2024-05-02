package com.example.application.views.list;

import com.example.application.data.Student;
import com.example.application.services.StudentService;
import com.example.application.data.StudentCourse;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PermitAll
// The student form layout for adding, editing, and deleting student information
@PageTitle("StudentForm | SMS")
public class StudentForm extends FormLayout {

    // Form fields
    TextField firstName = new TextField("First name"); // Text field for entering first name
    TextField lastName = new TextField("Last name"); // Text field for entering last name
    TextField age = new TextField("Age"); // Text field for entering age
    ComboBox<StudentCourse> course = new ComboBox<>("Program"); // ComboBox for selecting student program
    ComboBox<String> gender = new ComboBox<>("Gender"); // ComboBox for selecting student gender
    EmailField email = new EmailField("Email"); // Email field for entering student email
    TextField studentNumber = new TextField("Student Number"); // Text field for entering student number

    // Buttons
    Button save = new Button("SAVE"); // Button for saving student data
    Button delete = new Button("DELETE"); // Button for deleting student data
    Button close = new Button("CLOSE"); // Button for closing the form

    // Binder for binding form fields to the Student class
    BeanValidationBinder<Student> binder = new BeanValidationBinder<>(Student.class);

    // Constructor
    public StudentForm(List<StudentCourse> courses) {
        // Add CSS class to the form layout
        addClassName("student-form");

        // Bind form fields to Student class properties
        binder.bindInstanceFields(this);

        // Set items and labels for course combobox
        course.setItems(courses); // Set courses as items in the combobox
        course.setItemLabelGenerator(StudentCourse::getName); // Set course name as label for each item
        course.setPlaceholder("Select Course"); // Set placeholder text for the combobox

        // Set items and placeholder for gender combobox
        gender.setItems("Male", "Female", "LGBT"); // Set gender options
        gender.setPlaceholder("Select Gender"); // Set placeholder text for the combobox

        // Add form fields and buttons to the form layout
        add(firstName, lastName, age, course, gender, email, studentNumber, createButtonsLayout());
    }

    // Method to validate form input and save student data
    private void validateAndSave() {
        if (binder.isValid()) { // Check if form fields are valid
            // Get student object from binder
            Student student = binder.getBean();
            if (student != null) {
                // Fire save event with the student object
                fireEvent(new SaveEvent(this, student));
                // Show success notification
                String notificationMessage = student.getFirstName() + " " + student.getLastName() + " added!";
                Notification notification = Notification.show(notificationMessage, 3000, Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                // Show error notification
                Notification notification = Notification.show("Error saving!", 3000, Position.TOP_CENTER);
            }
        }
    }

    // Method to create the layout for buttons
    private Component createButtonsLayout() {
        // Set theme variants for buttons
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // Set primary theme for save button
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR); // Set error theme for delete button
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY); // Set tertiary theme for close button

        // Add click shortcuts for buttons
        save.addClickShortcut(Key.ENTER); // Add Enter key shortcut for save button
        close.addClickShortcut(Key.ESCAPE); // Add Escape key shortcut for close button

        // Add click listeners for buttons
        save.addClickListener(event -> validateAndSave()); // Validate and save data when save button is clicked
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean()))); // Fire delete event when delete button is clicked
        close.addClickListener(event -> fireEvent(new CloseEvent(this))); // Fire close event when close button is clicked

        // Enable save button based on binder's validation status
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        // Return a horizontal layout with buttons
        return new HorizontalLayout(save, delete, close);
    }

    // Method to set the student data in the form
    public void setStudent(Student student) {
        binder.setBean(student); // Set student data to the binder
    }

    // Abstract class for events related to the student form
    public static abstract class StudentFormEvent extends ComponentEvent<StudentForm> {
        private Student student;

        protected StudentFormEvent(StudentForm source, Student student) {
            super(source, false);
            this.student = student;
        }

        public Student getStudent() {
            return student;
        }
    }

    // Event class for saving student data
    public static class SaveEvent extends StudentFormEvent {
        SaveEvent(StudentForm source, Student student) {
            super(source, student);
        }
    }

    // Event class for deleting student data
    public static class DeleteEvent extends StudentFormEvent {
        DeleteEvent(StudentForm source, Student student) {
            super(source, student);
        }
    }

    // Event class for closing the form
    public static class CloseEvent extends StudentFormEvent {
        CloseEvent(StudentForm source) {
            super(source, null);
        }
    }

    // Method to add a listener for the delete event
    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener); // Add listener for delete event
    }

    // Method to add a listener for the save event
    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener); // Add listener for save event
    }

    // Method to add a listener for the close event
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener); // Add listener for close event
    }
}
