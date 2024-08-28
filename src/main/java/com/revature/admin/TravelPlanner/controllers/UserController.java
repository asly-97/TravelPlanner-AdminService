package com.revature.admin.TravelPlanner.controllers;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.exceptions.UserNotFoundException;
import com.revature.admin.TravelPlanner.models.User;
import com.revature.admin.TravelPlanner.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserDetails(@PathVariable UUID userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        return ResponseEntity.accepted().body(user);
    }

    @ExceptionHandler(CustomException.class)
    private ResponseEntity<String> handleCustomExceptions(CustomException e){
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

}
