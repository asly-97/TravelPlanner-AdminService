package com.revature.admin.TravelPlanner.config;

import com.revature.admin.TravelPlanner.enums.AdminRole;
import com.revature.admin.TravelPlanner.security.AdminUserDetailsService;
import com.revature.admin.TravelPlanner.security.PasswordEncoderProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // JWT filter to handle token-based authentication
    @Autowired
    JwtRequestFilter adminJwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Disable CSRF configuration (not needed for a stateless API)
        httpSecurity.cors(withDefaults());
        httpSecurity.csrf(csrf -> csrf.disable());
        // Define admin-specific authorization rules for HTTP requests
        httpSecurity.authorizeHttpRequests(request -> {
            request
                    // Allow preflight requests
                    .requestMatchers(HttpMethod.OPTIONS, "/admin/**").permitAll()
                    // Allow unrestricted access to user registration and login endpoints
                    .requestMatchers(HttpMethod.POST, "/admin/register", "/admin/login").permitAll()
                    // Require authentication for all other admin-specific /admin/** URLs
                    .requestMatchers("/admin/**").authenticated()
                    // Require master role for master-specific /master/** URLs
                    .requestMatchers("/master/**").hasAuthority(AdminRole.ROLE_MASTER.name())
                    // Deny all other requests outside admin-specific /admin/**
                    .anyRequest().denyAll();
        });

        // Set session management to stateless (no session will be created)
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add the admin-specific JWT filter before the default username/password authentication filter
        httpSecurity.addFilterBefore(adminJwtRequestFilter, UsernamePasswordAuthenticationFilter.class);



        // Build and return the admin-specific SecurityFilterChain
        return httpSecurity.build();
    }

    // Admin-specific service to load Admin user data
    @Autowired
    AdminUserDetailsService userDetailsService;

    // Password encoder password encryption
    @Autowired
    PasswordEncoderProvider passwordEncoder;

    @Bean
    // Provides Admin authentication mechanism based on user details and password encoder
    AuthenticationProvider authProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // Set the password encoder
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        // Set the admin-specific user details service
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        // Return the admin-specific authentication provider
        return daoAuthenticationProvider;
    }

    @Bean
    AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_MASTER > ROLE_ADMIN");
        return roleHierarchy;
    }

}
