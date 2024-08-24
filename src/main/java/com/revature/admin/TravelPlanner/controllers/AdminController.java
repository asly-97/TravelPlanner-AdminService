package com.revature.admin.TravelPlanner.controllers;

import com.revature.admin.TravelPlanner.DTOs.OutgoingAdminDTO;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    //Service Variable(s)
    private AdminService as;

    @Autowired
    AuthController authController;

    //Constructor
    @Autowired
    public AdminController(AdminService as) {
        this.as = as;
    }

    //Mappings
    @GetMapping
    public List<OutgoingAdminDTO> getAllAdmins() {
        return as.getAllAdmins();
    }

    @PostMapping("/register")
    public ResponseEntity<Admin> createAdmin(@RequestBody @Valid Admin admin)throws CustomException {
        Admin returningAdmin =  as.createAdmin(admin);
        return ResponseEntity.status(201).body(returningAdmin);
    }

    @PatchMapping
    public ResponseEntity<Object> updateLoggedInAdminProfile(@RequestBody Map<String,String> newAdmin) throws CustomException {
        try{
            Admin admin = as.updateAdminById(loggedInAdminId(), newAdmin);
            return ResponseEntity.ok(admin);
        }catch(Exception e){
            return ResponseEntity.status(400).body(null);
        }
    }

    private int loggedInAdminId() throws CustomException {

        Admin authAdmin = authController.getAuthenticatedAdmin();

        if(authAdmin != null){
            return authAdmin.getAdminId();
        }

        return 0;

    }


    // handles all the custom exceptions
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException( CustomException e){
        return ResponseEntity.status(e.getStatus()).body(e.getMsg());
    }



}
