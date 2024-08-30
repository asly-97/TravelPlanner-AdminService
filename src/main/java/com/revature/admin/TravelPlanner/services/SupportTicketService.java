package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.NoteDAO;
import com.revature.admin.TravelPlanner.DAOs.SupportTicketDAO;
import com.revature.admin.TravelPlanner.DAOs.UserDAO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingNoteDTO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingSupportTicketDTO;
import com.revature.admin.TravelPlanner.enums.TicketStatus;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.SupportTicketNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.UserNotFoundException;
import com.revature.admin.TravelPlanner.mappers.OutgoingNoteMapper;
import com.revature.admin.TravelPlanner.mappers.OutgoingSupportTicketMapper;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.models.Note;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import com.revature.admin.TravelPlanner.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class SupportTicketService {

    //Because note is OneToOne, creating static object to save memory
    private static OutgoingNoteDTO noteDTO = null;

    private SupportTicketDAO ticketDAO;
    private NoteDAO noteDAO;
    private UserDAO userDAO;

    private AuthService authService;

    private OutgoingSupportTicketMapper ticketMapper;
    private OutgoingNoteMapper noteMapper;

    @Autowired
    public SupportTicketService(SupportTicketDAO ticketDAO, NoteDAO noteDAO, UserDAO userDAO, AuthService authService,
                                OutgoingSupportTicketMapper ticketMapper, OutgoingNoteMapper noteMapper) {
        this.ticketDAO = ticketDAO;
        this.noteDAO = noteDAO;
        this.userDAO = userDAO;
        this.authService = authService;
        this.ticketMapper = ticketMapper;
        this.noteMapper = noteMapper;
    }

    //-------------Get Methods------------
    //
    //

    //Method to return a SupportTicket by its id with the associated User using userId and email
    public OutgoingSupportTicketDTO getSupportTicketById(UUID id) throws SupportTicketNotFoundException {

        Optional<SupportTicket> st = ticketDAO.findById(id);

        if (st.isPresent()) {

            // Get corresponding note if exists
            Optional<Note> note = noteDAO.findBySupportTicketSupportTicketId(id);
            note.ifPresent(value -> noteDTO = noteMapper.toDto(value));
            OutgoingSupportTicketDTO outgoingTicket = ticketMapper.toDto(st.get(), noteDTO);
            noteDTO = null;
            return outgoingTicket;

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
            Optional<Note> note = noteDAO.findBySupportTicketSupportTicketId(st.getSupportTicketId());
            note.ifPresent(value -> noteDTO = noteMapper.toDto(value));
            returnList.add(ticketMapper.toDto(st,noteDTO));
            noteDTO = null;

        }

        return returnList;

    }

    //Return a list of all Support Tickets submitted by a User
    public List<OutgoingSupportTicketDTO> getAllSupportTicketsByUserId(UUID id) throws UserNotFoundException{

        Optional<User> user = userDAO.findById(id);

        if (user.isPresent()){

            List<SupportTicket> stList = ticketDAO.findAllByUserUserId(id);
            List<OutgoingSupportTicketDTO> outgoingTicketList = new ArrayList<OutgoingSupportTicketDTO>();
            for (SupportTicket st: stList) {

                Optional<Note> note = noteDAO.findBySupportTicketSupportTicketId(st.getSupportTicketId());
                note.ifPresent(value -> noteDTO = noteMapper.toDto(value));
                outgoingTicketList.add(ticketMapper.toDto(st, noteDTO));
                noteDTO = null;

            }

            return outgoingTicketList;

        } else {
            throw new UserNotFoundException(id);

        }


    }

    //-------------Patch Methods------------
    //
    //


    // Resolve a Support Ticket
    public OutgoingSupportTicketDTO resolve(UUID ticketId, String noteText) throws SupportTicketNotFoundException,
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
        ticket.setResolvedAt(new Date());
        ticketDAO.save(ticket); // Persist the changes

        return ticketMapper.toDto(ticket,noteMapper.toDto(note));
    }


    //-------------Delete Methods------------
    //
    //

    //Method to delete a Support Ticket from the Database
    public OutgoingSupportTicketDTO delete(UUID id) throws SupportTicketNotFoundException {

        Optional<SupportTicket> toDeleteTicket = ticketDAO.findById(id);

        if (toDeleteTicket.isPresent()) {

            // Get corresponding note if exists
            Optional<Note> note = noteDAO.findBySupportTicketSupportTicketId(id);
            note.ifPresent(value -> {
                noteDTO = noteMapper.toDto(value);
                noteDAO.delete(value);
            });

            ticketDAO.delete(toDeleteTicket.get());
            OutgoingSupportTicketDTO outgoingTicket = ticketMapper.toDto(toDeleteTicket.get(),noteDTO);
            noteDTO = null;
            return outgoingTicket;

        } else {
            throw new SupportTicketNotFoundException(id);

        }

    }

}//End of SupportTicketService
