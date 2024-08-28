package com.revature.admin.TravelPlanner.controllers;

import com.revature.admin.TravelPlanner.DTOs.OutgoingSupportTicketDTO;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.exceptions.InvalidStatusException;
import com.revature.admin.TravelPlanner.exceptions.SupportTicketNotFoundException;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import com.revature.admin.TravelPlanner.services.SupportTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/support")
@CrossOrigin                    //TODO::Create extra security safety
public class SupportTicketController {
    Logger log = LoggerFactory.getLogger(SupportTicketController.class);
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
        log.debug("Endpoint GET ./support reached, id={}",id);

        try {

            OutgoingSupportTicketDTO returnSupportTicket = sts.getSupportTicketById(id);
            return ResponseEntity.ok(returnSupportTicket);

        } catch (SupportTicketNotFoundException e) {
            log.warn("SupportTicketNotFoundException was thrown: {}", e.getMessageText());
            return ResponseEntity.status(400).body(e.getMessageText());

        }

    }

    //Get All Support Tickets
    @GetMapping("/all")
    public ResponseEntity<List<OutgoingSupportTicketDTO>> getAllSupportTickets() {
        log.debug("Endpoint GET ./support/all reached");
        return ResponseEntity.ok(sts.getAlSupportTickets());
    }

    /*
    *   ==============PATCH MAPPINGS=================
    */

    //Resolve a Support Ticket
    // Resolving a ticket can by sending a request to this endpoint
    // And include an optional note text
    @PatchMapping("/{ticketId}")
    public ResponseEntity<OutgoingSupportTicketDTO> resolveTicket(
            @PathVariable int ticketId,
            @RequestBody String noteText) throws SupportTicketNotFoundException, AdminNotFoundException {
        log.debug("Endpoint PATCH ./support/{} reached",ticketId);

        OutgoingSupportTicketDTO resolvedTicket = sts.resolve(ticketId, noteText);

        return ResponseEntity.ok(resolvedTicket);
    }

    /*
    *   ==============DELETE MAPPINGS=================
    */

    //Delete a Support Ticket from the DB
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<?> delete(int id){
        log.debug("Endpoint DELETE ./support/{} reached",id);

        try{
            return ResponseEntity.ok(sts.delete(id));

        } catch (SupportTicketNotFoundException e) {
            log.warn("SupportTicketNotFoundException was thrown: {}", e.getMessageText());
            return ResponseEntity.ok(e.getMessageText());

        }

    }

    // handles all the custom exceptions
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException( CustomException e){
        log.warn("Exception was thrown: {}", e.getMessageText());
        return ResponseEntity.status(e.getStatus()).body(e.getMessageText());
    }

}
