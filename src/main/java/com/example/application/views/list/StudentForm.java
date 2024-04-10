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

import java.util.List;
import java.util.logging.Logger;

@PageTitle("StudentForm | TAG")
public class StudentForm extends FormLayout {

  private static final Logger LOGGER = Logger.getLogger(StudentView.class.getName());

  TextField firstName = new TextField("First name");
  TextField lastName = new TextField("Last name");
  TextField age = new TextField("Age");
  ComboBox<StudentCourse> course = new ComboBox<>("Program");
  ComboBox<String> gender = new ComboBox<>("Gender");
  EmailField email = new EmailField("Email");
  TextField studentNumber = new TextField("Student Number");

  Button save = new Button("SAVE");
  Button delete = new Button("DELETE");
  Button close = new Button("CLOSE");

  BeanValidationBinder<Student> binder = new BeanValidationBinder<>(Student.class);

  public StudentForm(List<StudentCourse> courses) {
    addClassName("student-form");
    binder.bindInstanceFields(this);

    course.setItems(courses);
    course.setItemLabelGenerator(StudentCourse::getName);
    course.setPlaceholder("Select Course");
    gender.setItems("Male","Female","LGBT");
    gender.setPlaceholder("Select Gender");

    add(firstName,
        lastName,
        age,
	course,
        gender,
        email,
	studentNumber,
        createButtonsLayout());
  }

    private void validateAndSave() {
    	if (binder.isValid()) {
            Student student = binder.getBean();
        	if (student != null){
            	   fireEvent(new SaveEvent(this, student));
            	   String firstName = student.getFirstName();
            	   String lastName = student.getLastName();
            	   String notificationMessage = firstName + " " + lastName + " added!";
	    	   LOGGER.info(firstName + " " + lastName + " added!");
	    	   Notification notification = Notification.show(notificationMessage, 3000, Position.TOP_CENTER);
		   notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
		   notification.addClassNames(
			LumoUtility.Padding.Horizontal.NONE,
			LumoUtility.Margin.Top.NONE,
			LumoUtility.Margin.Right.NONE,
			LumoUtility.Margin.Left.NONE
		   );
        	} else {
            	   LOGGER.warning("Student object is null in validateAndSave method!");
		   Notification notification = Notification.show("Error saving!", 3000, Position.TOP_CENTER);
        	}
    	 }
    }

  private Component createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickShortcut(Key.ENTER);
    close.addClickShortcut(Key.ESCAPE);

    save.addClickListener(event -> validateAndSave());
    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));

    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, delete, close);
  }

  public void setStudent(Student student) {
        binder.setBean(student);
  }

   //ABSTRACT
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

  public static class SaveEvent extends StudentFormEvent {
        SaveEvent(StudentForm source, Student student) {
        	super(source, student);
  	}
  }

  public static class DeleteEvent extends StudentFormEvent {
        DeleteEvent(StudentForm source, Student student) {
        	super(source, student);
  	}
  }

  public static class CloseEvent extends StudentFormEvent {
        CloseEvent(StudentForm source) {
        	super(source, null);
  	}
  }

  public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
  }

  public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
  }

  public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
  }
}
