package com.revature.admin.TravelPlanner.controllers;

import com.revature.admin.TravelPlanner.DTOs.OutgoingSupportTicketDTO;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.InvalidStatusException;
import com.revature.admin.TravelPlanner.exceptions.SupportTicketNotFoundException;
import com.revature.admin.TravelPlanner.services.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/support")
@CrossOrigin                    //TODO::Create extra security safety
public class SupportTicketController {

    //Model Variables
    private SupportTicketService sts;

    //Constructor
    @Autowired
    public SupportTicketController(SupportTicketService sts) {
        this.sts = sts;
    }


    /*
    *   ==============GET MAPPINGS=================
    */

    //Return Support Ticket by Id
    @GetMapping
    public ResponseEntity<?> getSupportTicketById( @RequestParam(name = "id") int id ) {

        try {

            OutgoingSupportTicketDTO returnSupportTicket = sts.getSupportTicketById(id);
            return ResponseEntity.ok(returnSupportTicket);

        } catch (SupportTicketNotFoundException e) {
            return ResponseEntity.status(400).body(e.getMessage());

        }

    }

    //Get All Support Tickets
    @GetMapping("/get/all")
    public ResponseEntity<List<OutgoingSupportTicketDTO>> getAllSupportTickets() {
        return ResponseEntity.ok(sts.getAlSupportTickets());
    }

    /*
    *   ==============PATCH MAPPINGS=================
    */

    //Resolve a Support Ticket
    @PatchMapping("/resolve/{id}")
    public ResponseEntity<?> resolve(@PathVariable int id, @RequestBody String type){

        try {

            OutgoingSupportTicketDTO resolvedTicket = sts.resolve(id, type);
            return ResponseEntity.ok(resolvedTicket);

        } catch (SupportTicketNotFoundException | InvalidStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());

        }

    }

    /*
    *   ==============DELETE MAPPINGS=================
    */

    //Delete a Support Ticket from the DB
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(int id){

        try{
            return ResponseEntity.ok(sts.delete(id));

        } catch (SupportTicketNotFoundException e) {
            return ResponseEntity.ok(e.getMessage());

        }

    }

}
