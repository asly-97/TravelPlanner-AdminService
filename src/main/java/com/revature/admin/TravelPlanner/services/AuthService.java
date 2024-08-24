package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.AdminDAO;
import com.revature.admin.TravelPlanner.DTOs.IncomingAdminDTO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingJwtDTO;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.mappers.AdminMapper;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // Inject the admin-specific authentication manager
    @Autowired
    //@Qualifier("adminAuthManager")
    AuthenticationManager authManager;

    // Inject the Data Access Object for the Admin entity
    @Autowired
    AdminDAO adminDAO;

    // Inject the JWT provider for generating tokens
    @Autowired
    JwtTokenProvider jwtProvider;

    @Autowired
    AdminMapper adminMapper;

    // Method to handle login for admin users
    public OutgoingJwtDTO login(IncomingAdminDTO adminDTO) throws AdminNotFoundException {

        // Extract email from the incoming DTO
        String email = adminDTO.getEmail();

        // Extract password from the incoming DTO
        String password = adminDTO.getPassword();

        // Create an unauthenticated authentication object using email and password
        Authentication unAuth = new UsernamePasswordAuthenticationToken(email, password);

        // Initialize an authenticated object to null
        Authentication auth = null;
        try {
            // Authenticate the unauthenticated object using the authentication manager
            auth = authManager.authenticate(unAuth);
        } catch (Exception e) {
            // Print the authentication exception message in case of failure
            // TODO: Custom Exception
            System.out.println("Authentication Exception: " + e.getMessage());
        }

        // Check if authentication was successful and the user is authenticated
        if (auth != null && auth.isAuthenticated()) {
            // Find the authenticated admin in the database using the email
            Admin authAdmin = adminDAO.findByEmail(adminDTO.getEmail()).get();

            // Generate a JWT token for the authenticated admin
            String token = jwtProvider.generateToken(authAdmin.getAdminId());

            // Return a DTO containing the JWT token, admin ID, and email
            return adminMapper.toDto(authAdmin,token);

        } else {
            // If login was unsuccessful, throw a UserNotFoundException with the provided email
            throw new AdminNotFoundException(adminDTO.getEmail());
        }
    }
}
