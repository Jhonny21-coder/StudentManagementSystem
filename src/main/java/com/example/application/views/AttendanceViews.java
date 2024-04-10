package com.example.application.views;

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

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

@Route("attendance")
public class AttendanceViews extends AppLayout {
    private final StudentService service;
    private final AttendanceService attendanceService;

    public AttendanceViews(StudentService service, AttendanceService attendanceService) {
	this.attendanceService = attendanceService;

	List<Student> students = StudentService.getAllStudents();

	Span header = new Span("Attendance");
	header.addClassName("header");

	addClassName("nav");

        Button attendanceListButton = new Button("Attendance List", new Icon(VaadinIcon.LIST), event -> showAttendanceList());
	attendanceListButton.addClassName("attendance-button");

	Button addAttendanceButton = new Button("Add New Attendance", new Icon(VaadinIcon.PLUS));
	addAttendanceButton.addClassName("add-button");
	addAttendanceButton.addClickListener(event -> {
	     for(Student student : students){
		 student.setStatus(false);
		 service.saveStudent(student);
	     }
	     getUI().ifPresent(ui -> ui.navigate(AddAttendanceView.class));
	});

	Button closeButton = new Button("Close", new Icon(VaadinIcon.CLOSE), event -> getUI().ifPresent(ui -> ui.navigate("")));
        closeButton.addClassName("close-button");

	HorizontalLayout headerLayout = new HorizontalLayout(header, attendanceListButton, addAttendanceButton, closeButton);
	headerLayout.setSpacing(true);

	addToNavbar(headerLayout);

	this.service = service;

        Grid<Student> grid = new Grid<>(Student.class, false);
	grid.setSizeFull();
	grid.addClassName("grid");

	grid.addComponentColumn(student -> {
	    Span indexSpan = new Span();
	    int index = students.indexOf(student) + 1;

	    indexSpan.setText(String.valueOf(index) + ".");

    	    return indexSpan;
        }).setHeader("No.");

	grid.addColumn(Student::getStudentNumber).setHeader("Student Number")
		.setAutoWidth(true);
        grid.addColumn(Student::getId).setHeader("ID")
                .setAutoWidth(true);
        grid.addColumn(Student::getFullName).setHeader("Full Name")
                .setAutoWidth(true);
	grid.addColumn(Student::getAbsent).setHeader("Absences")
		.setAutoWidth(true);

	Collections.sort(students, Comparator.comparing(Student::getFirstName));

        grid.setItems(students);

        setContent(grid);
    }

    private void showAttendanceList() {
        // Retrieve and display past attendance records
        List<Attendance> pastAttendance = attendanceService.getAllAttendance();

        Grid<Attendance> attendanceGrid = new Grid<>(Attendance.class, false);
	attendanceGrid.addClassName("grid");

	attendanceGrid.addColumn(Attendance::getAttendanceDate).setHeader("Attendance Date")
                .setAutoWidth(true);
	attendanceGrid.addColumn(Attendance::getFormattedAttendanceTime).setHeader("Attendance Time")
                .setAutoWidth(true);
        attendanceGrid.addColumn(Attendance::getAbsentStudentIds).setHeader("ID of Absent Student/s")
                .setAutoWidth(true);
        attendanceGrid.setItems(pastAttendance);

	Span dialogHeader = new Span("Attendance List");
        Dialog dialog = new Dialog();
	dialog.addClassName("attendance-dialog");
	dialog.getHeader().add(dialogHeader);
        dialog.setWidth("800px");
        dialog.add(attendanceGrid);
        dialog.open();
    }
}
