package com.revature.admin.TravelPlanner.security;

import com.revature.admin.TravelPlanner.DAOs.AdminDAO;
import com.revature.admin.TravelPlanner.models.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    // Inject the AdminDAO to interact with the database
    @Autowired
    public AdminDAO adminDAO;

    // Load admin details by username (email in this case)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Find the admin by email (username) or throw an exception if not found
        Admin admin = adminDAO.findByEmail(username).orElseThrow(() -> {
            return new UsernameNotFoundException("No admin with email " + username);
        });

        // Return a AdminUserDetailsImpl object containing the admin's details
        return new AdminUserDetails(admin);
    }

    // Load admin details by user ID
    public AdminUserDetails loadUserByUserId(UUID userId) throws UsernameNotFoundException {

        // Find the admin by ID or throw an exception if not found
        Admin admin = adminDAO.findById(userId).orElseThrow(() -> {
            return new UsernameNotFoundException("No Admin with id: " + userId);
        });

        // Return a AdminUserDetailsImpl object containing the admin's details
        return new AdminUserDetails(admin);
    }
}
