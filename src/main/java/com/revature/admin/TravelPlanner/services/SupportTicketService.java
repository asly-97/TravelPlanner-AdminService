package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.AdminDAO;
import com.revature.admin.TravelPlanner.DAOs.NoteDAO;
import com.revature.admin.TravelPlanner.DAOs.SupportTicketDAO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingNoteDTO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingSupportTicketDTO;
import com.revature.admin.TravelPlanner.enums.TicketStatus;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.SupportTicketNotFoundException;
import com.revature.admin.TravelPlanner.mappers.OutgoingNoteMapper;
import com.revature.admin.TravelPlanner.mappers.OutgoingSupportTicketMapper;
import com.revature.admin.TravelPlanner.mappers.StatusMapper;
import com.revature.admin.TravelPlanner.mappers.TypeMapper;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.models.Note;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class SupportTicketService {

    @Autowired
    private AuthService authService;

    @Autowired
    private SupportTicketDAO ticketDAO;

    @Autowired
    private NoteDAO noteDAO;

    @Autowired
    private NoteService noteService;

    @Autowired
    private OutgoingSupportTicketMapper ticketMapper;

    @Autowired
    private OutgoingNoteMapper noteMapper;




    //-------------Get Methods------------
    //
    //

    //Method to return a SupportTicket by its id with the associated User using userId and email
    public OutgoingSupportTicketDTO getSupportTicketById(int id) throws SupportTicketNotFoundException {

        Optional<SupportTicket> st = ticketDAO.findById(id);

        if (st.isPresent()) {

            // Get corresponding note if exists
            Note note = noteService.getBySupportTicketId(id);
            OutgoingNoteDTO noteDTO = null;
            if(note != null){
                noteDTO = noteMapper.toDto(note);
            }

            return ticketMapper.toDto(st.get(),noteDTO);

        } else {
            throw new SupportTicketNotFoundException(id);

        }

    }

    //Methods to return all SupportTickets for Admins
    public List<OutgoingSupportTicketDTO> getAlSupportTickets() {

        //Instantiate Lists
        List<SupportTicket> stl = ticketDAO.findAll();
        List<OutgoingSupportTicketDTO> returnList = new ArrayList<>();

        for (SupportTicket st: stl) {
            // Get corresponding note if exists
            Note note = noteService.getBySupportTicketId(st.getSupportTicketId());
            OutgoingNoteDTO noteDTO = null;
            if(note != null){
                noteDTO = noteMapper.toDto(note);
            }
            returnList.add(ticketMapper.toDto(st,noteDTO));

        }

        return returnList;

    }

    //-------------Patch Methods------------
    //
    //


    // Resolve a Support Ticket
    public OutgoingSupportTicketDTO resolve(int ticketId, String noteText) throws SupportTicketNotFoundException,
            AdminNotFoundException {

        SupportTicket ticket = ticketDAO
                .findById(ticketId)
                .orElseThrow(()->new SupportTicketNotFoundException(ticketId));

        // Will be resolved by this authenticated admin
        Admin loggedInAdmin = authService.getLoggedInAdmin();

        // Create a note for the ticket
        noteText = noteText == null? "Your ticket was resolved." : noteText;
        Note note = new Note();
        note.setText(noteText);
        note.setAdmin(loggedInAdmin); // Attach to the current Admin
        note.setSupportTicket(ticket); // Attach it to the support ticket
        noteDAO.save(note); // Save the note

        // Resolving the ticket
        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setResolvedAt(Instant.now().toEpochMilli());
        ticketDAO.save(ticket); // Persist the changes

        OutgoingNoteDTO noteDTO = noteMapper.toDto(note);

        return ticketMapper.toDto(ticket,noteDTO);
    }


    //-------------Delete Methods------------
    //
    //

    //Method to delete a Support Ticket from the Database
    public OutgoingSupportTicketDTO delete(int id) throws SupportTicketNotFoundException {

        Optional<SupportTicket> toDeleteTicket = ticketDAO.findById(id);

        if (toDeleteTicket.isPresent()) {

            // Get corresponding note if exists
            Note note = noteService.getBySupportTicketId(id);
            OutgoingNoteDTO noteDTO = null;
            if(note != null){
                noteDTO = noteMapper.toDto(note);
                noteDAO.delete(note);
            }
            ticketDAO.delete(toDeleteTicket.get());
            return ticketMapper.toDto(toDeleteTicket.get(),noteDTO);

        } else {
            throw new SupportTicketNotFoundException(id);

        }

    }

}//End of SupportTicketService
