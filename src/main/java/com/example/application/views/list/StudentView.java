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

import java.util.logging.Logger;

@Route(value="", layout = MainLayout.class)
@PageTitle("Students | Student Info")
public class StudentView extends VerticalLayout {

    private static final Logger LOGGER = Logger.getLogger(StudentView.class.getName());

    Grid<Student> grid = new Grid<>(Student.class);
    TextField filterText = new TextField();
    StudentForm form;
    StudentService service;

    public StudentView(StudentService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

	filterText.setSuffixComponent(new Icon(VaadinIcon.SEARCH));

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new StudentForm(service.findAllCourses());
        form.setWidth("25em");
        form.addSaveListener(this::saveStudent);
        form.addDeleteListener(this::deleteStudent);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveStudent(StudentForm.SaveEvent event) {
        service.saveStudent(event.getStudent());
        updateList();
        closeEditor();
    }

    private void deleteStudent(StudentForm.DeleteEvent event) {
        service.deleteStudent(event.getStudent());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("studentNumber", "firstName", "lastName","gender", "email");
	grid.addColumn(student -> student.getCourse().getName()).setHeader("Program");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
            editStudent(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Search by name...");
	filterText.addClassName("filter-field");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addStudentButton = new Button("Add Student", new Icon(VaadinIcon.PLUS));
        addStudentButton.addClickListener(click -> addStudent());
	addStudentButton.addClassName("addstudent-button");

        var toolbar = new HorizontalLayout(filterText, addStudentButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editStudent(Student student){

        if (student == null) {
	    LOGGER.warning("Student object is null in editStudent method!");
            closeEditor();
        } else {
	    LOGGER.info("Editing student: " + student.getFirstName() + " " + student.getLastName());
            form.setStudent(student);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setStudent(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addStudent() {
        grid.asSingleSelect().clear();
        editStudent(new Student());
    }


    private void updateList() {
        grid.setItems(service.findAllStudents(filterText.getValue()));
    }
}
