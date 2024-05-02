package com.example.application.views;
/* This class is the login form of the application.
*/
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.login.AbstractLogin;

@Route("login") // Set route
@PageTitle("Login | SMS") // Set page title
@AnonymousAllowed // Allow all users to access this login form
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
	// Create a login form component
	private final LoginForm login = new LoginForm();

	// Constructor initializing the login form
	public LoginView(){
	   addClassName("login-view"); // Add CSS class for styling
	   setSizeFull(); // Set full size
	   setAlignItems(Alignment.CENTER); // Align items to center
	   setJustifyContentMode(JustifyContentMode.CENTER); // Justify content to center

	   // Set login form action
	   login.setAction("login");

	   // Application's username
	   String adminUsername = "studentmanagementsystem";
	   // Application's password
	   String adminPassword = "marahay_santos_ylar@BSMEC-1A";

	   // Add listener to handle clicking login button
	   login.addLoginListener(event -> {
	      // Get entered username
	      String username = event.getUsername();
	      // Get entered password
	      String password = event.getPassword();

	     // Check and compare if entered username and password is equals to application's credentials
              if (username.equals(adminUsername) && password.equals(adminPassword)) {
              	  // If so, show notification indicating a successful login operation
	          Notification.show("Login successfully!", 3000, Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	      }
	   });

	  // Add login page and login form to the layout
	   add(new H2("Student Management System"), login);
	}

	// Method to observe login form before entering to the application's main page
	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
	   // Check if query parameters from login form contains error then inform user if there is an error
	   if(beforeEnterEvent.getLocation()
           .getQueryParameters()
           .getParameters()
           .containsKey("error")) {
               // Set the in built login error message
               login.setError(true);
            }
	}
}
