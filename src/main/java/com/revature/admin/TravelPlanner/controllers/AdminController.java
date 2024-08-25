package com.revature.admin.TravelPlanner.controllers;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    //Service Variable(s)
    protected AdminService as;

    @Autowired
    AuthController authController;

    //Constructor
    @Autowired
    public AdminController(@Qualifier("adminService") AdminService as) {
        this.as = as;
    }

    @PatchMapping()
    public ResponseEntity<Object> updateLoggedInAdminProfile(@RequestBody Map<String,String> newAdmin) throws CustomException {
        try{
            Admin admin = as.updateLoggedInAdmin(newAdmin);
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
        return ResponseEntity.status(e.getStatus()).body(e.getMessageText());
    }



}
