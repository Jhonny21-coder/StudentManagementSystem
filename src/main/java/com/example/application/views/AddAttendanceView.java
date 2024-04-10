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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Route("addAttendance")
public class AddAttendanceView extends AppLayout {
    private final StudentService service;
    private final AttendanceService attendanceService;

    public AddAttendanceView(StudentService service, AttendanceService attendanceService) {
        this.attendanceService = attendanceService;

	Span date = new Span("[ " + String.valueOf(LocalDate.now()) + " ]");
	date.addClassName("date");
        Span header = new Span("Attendance ");
	header.addClassName("add-header");

        addClassName("nav");

        this.service = service;

	List<Student> students = StudentService.getAllStudents();
	List<Long> idList = new ArrayList<>();

	Button closeButton = new Button("Close", new Icon(VaadinIcon.CLOSE), event -> getUI().ifPresent(ui -> ui.navigate(AttendanceViews.class)));
      	closeButton.addClassName("close-button");

        Button saveButton = new Button("Save", new Icon(VaadinIcon.CHECK));
        saveButton.addClassName("save-button");
        saveButton.addClickListener(event -> {
             for(Student student : students){
		 if(student.getStatus() == false){
		    idList.add(student.getId());
		    student.setAbsent(student.getAbsent() + 1);
		    service.saveStudent(student);
		  }
	     }

	     ZoneId zoneId = ZoneId.of("America/New_York");

             // Get the current time in the specified time zone
             LocalTime localTime = LocalTime.now(zoneId);

	     Attendance attendance = new Attendance();
	     attendance.setAttendanceDate(LocalDate.now());
	     attendance.setAttendanceTime(localTime);
	     attendance.setAbsentStudentIds(idList);
	     attendanceService.saveAttendance(attendance);
             getUI().ifPresent(ui -> ui.navigate(AttendanceViews.class));
        });

	HorizontalLayout headerLayout = new HorizontalLayout(
		header, date, saveButton, closeButton);
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

        grid.addComponentColumn(student -> {
            Checkbox checkbox = new Checkbox();

            checkbox.setValue(student.getStatus());
            checkbox.addValueChangeListener(event -> {
                boolean newStatus = event.getValue();

                student.setStatus(newStatus);
                service.saveStudent(student);

                // Update the badge color live
                grid.getDataProvider().refreshItem(student);
            });
            return checkbox;
        }).setHeader("Mark as Present");

        grid.addColumn(Student::getId).setHeader("ID")
                .setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(Student::getFullName).setHeader("Full Name")
                .setAutoWidth(true);
        grid.addColumn(createStatusComponentRenderer()).setHeader("Is Present?")
                .setAutoWidth(true);

        Collections.sort(students, Comparator.comparing(Student::getFirstName));

        grid.setItems(students);

        setContent(grid);
    }

    private static final SerializableBiConsumer<Span, Student> statusComponentUpdater = (
            span, student) -> {
        boolean isAvailable = student.getStatus() == true;
        String theme = String.format("badge %s", isAvailable ? "success" : "error");
        span.getElement().setAttribute("theme", theme);

        span.setText(String.valueOf(student.getStatus()));
    };

    private static ComponentRenderer<Span, Student> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }
}
