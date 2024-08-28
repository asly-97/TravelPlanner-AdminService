package com.revature.admin.TravelPlanner.controllers;

import com.revature.admin.TravelPlanner.DTOs.OutgoingAdminDTO;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.services.MasterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/master/admin")
@PreAuthorize("hasRole('ROLE_MASTER')")
@CrossOrigin(origins = "*")
public class MasterController extends AdminController{

    private MasterService masterService;

    @Autowired
    public MasterController(MasterService ms) {
        super(ms);
        this.masterService = ms;
    }

    //Mappings
    @GetMapping
    public List<OutgoingAdminDTO> getAllAdmins() {
        return masterService.getAllAdmins();
    }

    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody @Valid Admin admin)throws CustomException {
        Admin returningAdmin =  masterService.createAdmin(admin);
        return ResponseEntity.status(201).body(returningAdmin);
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<String> deleteAdmin(@PathVariable UUID adminId) throws CustomException{
        String message = masterService.deleteAdmin(adminId);
        return ResponseEntity.accepted().body(message);
    }
}
