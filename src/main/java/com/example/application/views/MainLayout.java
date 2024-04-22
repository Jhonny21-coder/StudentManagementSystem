package com.example.application.views;

import com.example.application.views.list.StudentView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.component.avatar.Avatar;

// Main layout of the project's view, including header, footer, and drawer
public class MainLayout extends AppLayout {

    // Constructor for initializing the layout
    public MainLayout() {
        // Apply a CSS class to the layout
        addClassName("main-layout");

        // Create the header and drawer
        createHeader(); // Method call to create the header section
        createDrawer(); // Method call to create the drawer section
    }

    // Method for creating the header section
    private void createHeader() {
        // Create the logo and project name for the header
        Avatar image = new Avatar(); // Create an avatar component for the logo
        image.setImage("./icons/icon.png"); // Set the image for the logo
        image.addClassName("avatar"); // Add CSS class for styling

        H1 name = new H1("Student Management System"); // Create the project name
        name.addClassName("main-header"); // Add CSS class for styling

        // Arrange components horizontally for the header
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), name, image); // Create a horizontal layout for the header
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER); // Set alignment
        header.expand(name); // Allow the project name to expand
        header.setWidthFull(); // Set full width
        header.addClassNames( // Add CSS classes for padding
            LumoUtility.Padding.Vertical.NONE, // No vertical padding
            LumoUtility.Padding.Horizontal.MEDIUM); // Medium horizontal padding

        // Add the header to the navigation bar
        addToNavbar(header); // Add the header to the top navigation bar
    }

    // Method for creating hyperlinks for navigation items in the drawer
    private RouterLink createLink(Class routeClass, VaadinIcon icon, String label){

        // Create a RouterLink for a specific route class with an icon and label
        RouterLink routerLink = new RouterLink(); // Create a router link
        routerLink.setHighlightCondition(HighlightConditions.sameLocation()); // Set highlight condition
        routerLink.setRoute(routeClass); // Set the route class

        // Add an icon and label to the RouterLink
        Icon routerIcon = new Icon(icon); // Create an icon
        routerIcon.addClassName("drawer-icon"); // Add CSS class for styling
        routerLink.add(routerIcon, new Span(label)); // Add icon and label
        routerLink.addClassName("drawer-link"); // Add CSS class for styling

        return routerLink; // Return the router link
    }

    // Method for creating the drawer section
    private void createDrawer() {
        // Create links for each feature in the drawer with icons and labels
        RouterLink student = createLink(StudentView.class, VaadinIcon.GROUP, "Students"); // Create a link for the Students view
        RouterLink attendance = createLink(AttendanceView.class, VaadinIcon.USER_CHECK, "Attendance"); // Create a link for the Attendance view
        RouterLink grades = createLink(GradesView.class, VaadinIcon.USER_STAR, "Grades"); // Create a link for the Grades view

        // Create a logo for the drawer
        Avatar image = new Avatar(); // Create an avatar component for the logo
        image.setImage("./icons/icon.png"); // Set the image for the logo
        image.addClassName("image"); // Add CSS class for styling

        // Arrange components vertically for the drawer
        VerticalLayout drawerLayout = new VerticalLayout(image, student, attendance, grades); // Create a vertical layout for the drawer

        // Add the drawer to the layout
        addToDrawer(drawerLayout); // Add the drawer to the side drawer
    }
}
