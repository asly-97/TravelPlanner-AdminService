package com.revature.admin.TravelPlanner.controllers;

import com.revature.admin.TravelPlanner.DTOs.OutgoingAdminDTO;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.services.MasterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/master/admin")
@PreAuthorize("hasRole('ROLE_MASTER')")
@CrossOrigin(origins = "*")
public class MasterController extends AdminController{

    Logger log = LoggerFactory.getLogger(AdminController.class);
    private MasterService masterService;

    @Autowired
    public MasterController(MasterService ms) {
        super(ms);
        this.masterService = ms;
    }

    //Mappings
    @GetMapping
    public List<OutgoingAdminDTO> getAllAdmins() {
        log.debug("Endpoint GET ./master/admin reached");
        return masterService.getAllAdmins();
    }

    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody @Valid Admin admin)throws CustomException {
        log.debug("Endpoint POST ./master/admin reached");
        Admin returningAdmin =  masterService.createAdmin(admin);
        return ResponseEntity.status(201).body(returningAdmin);
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<String> deleteAdmin(@PathVariable int adminId) throws CustomException{
        log.debug("Endpoint DELETE ./master/admin/{} reached",adminId);
        String message = masterService.deleteAdmin(adminId);
        return ResponseEntity.accepted().body(message);
    }
}
