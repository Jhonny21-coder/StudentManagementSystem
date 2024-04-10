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

public class MainLayout extends AppLayout {

    public MainLayout() {
	addClassName("main-layout");
        createHeader();
        createDrawer();
    }

    private void createHeader() {
	Avatar image = new Avatar();
        image.setImage("./icons/icon.png");
        image.addClassName("avatar");

        H1 logo = new H1("Student Management System");
	logo.addClassName("main-header");

        var header = new HorizontalLayout(new DrawerToggle(), logo, image);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);

    }

    private RouterLink createLink(Class routeClass, VaadinIcon icon, String label){
        RouterLink routerLink = new RouterLink();
        routerLink.setHighlightCondition(HighlightConditions.sameLocation());
        routerLink.setRoute(routeClass);
        Icon routerIcon = new Icon(icon);
        routerIcon.addClassName("drawer-icon");
        routerLink.add(routerIcon, new Span(label));
        routerLink.addClassName("drawer-link");

        return routerLink;
    }

    private void createDrawer() {
	RouterLink student = createLink(StudentView.class, VaadinIcon.GROUP, "Students");
	RouterLink attendance = createLink(AttendanceViews.class, VaadinIcon.USER_CHECK, "Attendance");
	RouterLink grades = createLink(AttendanceViews.class, VaadinIcon.USER_STAR, "Grades");

	Avatar image = new Avatar();
        image.setImage("./icons/icon.png");
        image.addClassName("image");

	VerticalLayout drawerLayout = new VerticalLayout(image, student, attendance, grades);

	addToDrawer(drawerLayout);
    }
}
