package com.example.application.security;
/* This class handle the authentication of the application
   to prevent unauthorized from accessing the application.
*/
import com.example.application.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// Enable security for this class
@EnableWebSecurity
// Annotation to mark this class as Spring Boot Configuration
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    // Method to handle application's authentication through requests
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// Allow images to be seen by all users by using http request
        http.authorizeHttpRequests(auth ->
	    auth.requestMatchers(
             AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/*.png")).permitAll()
	);
	// Configure http request
        super.configure(http);
        // Set authentication login page
        setLoginView(http, LoginView.class);
    }

    // Methos to handle application's username and password
    @Bean
    public UserDetailsService users() {
    	// Create a fixed credentials
        UserDetails admin = User.builder()
	    .username("studentmanagementsystem")
            .password("{bcrypt}$2a$10$Caj4y0oXWxUZo4a1HU8UlOz5odO1HmgEGALX/TjfGaaYJKo/vLLwe")
            .roles("USER", "ADMIN")
            .build();

	// Return credentials in application's memory
        return new InMemoryUserDetailsManager(admin);
    }
}
