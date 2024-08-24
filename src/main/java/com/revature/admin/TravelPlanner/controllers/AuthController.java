package com.revature.admin.TravelPlanner.controllers;

import com.revature.admin.TravelPlanner.DTOs.IncomingAdminDTO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingJwtDTO;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.services.AuthService;
import com.revature.admin.TravelPlanner.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    AdminService adminService;

    @PostMapping("/admin/login")
    public ResponseEntity<OutgoingJwtDTO> auth(@RequestBody IncomingAdminDTO loginDTO)
            throws AdminNotFoundException { //TODO: Admin Not Found Exception
        OutgoingJwtDTO jwtAdminDTO = authService.login(loginDTO);
        return ResponseEntity.status(201).body(jwtAdminDTO);
    }

    public Admin getAuthenticatedAdmin() throws CustomException {
        // Get the authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is not null and if the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {

            // Since it's a JWT token-based auth
            // the principal(user/username) will be a string
            // Get the username(email) of the authenticated user
            String email =  authentication.getPrincipal().toString();

            return adminService.getAdminByEmail(email);
        }
        return null;
    }


    // handles all the custom exceptions
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException( CustomException e){
        return ResponseEntity.status(e.getStatus()).body(e.getMsg());
    }



}

