package com.revature.admin.TravelPlanner.controllers;

import com.revature.admin.TravelPlanner.DTOs.IncomingAdminDTO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingJwtDTO;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.services.AuthService;
import com.revature.admin.TravelPlanner.services.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    MasterService adminService;

    @PostMapping
    public ResponseEntity<OutgoingJwtDTO> auth(@RequestBody IncomingAdminDTO loginDTO)
            throws AdminNotFoundException { //TODO: Admin Not Found Exception
        OutgoingJwtDTO jwtAdminDTO = authService.login(loginDTO);
        return ResponseEntity.status(201).body(jwtAdminDTO);
    }

    public Admin getAuthenticatedAdmin() throws CustomException {
        return authService.getLoggedInAdmin();
    }


    // handles all the custom exceptions
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException( CustomException e){
        return ResponseEntity.status(e.getStatus()).body(e.getMessageText());
    }



}

