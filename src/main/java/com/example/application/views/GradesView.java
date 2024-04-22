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

@Route("grades")
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
    	//dialog.setWidth("800px");
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
	if(student.getFullName().endsWith("s")){
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
}
