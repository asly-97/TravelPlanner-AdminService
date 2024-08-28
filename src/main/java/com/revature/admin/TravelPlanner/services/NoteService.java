package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.NoteDAO;
import com.revature.admin.TravelPlanner.DAOs.SupportTicketDAO;
import com.revature.admin.TravelPlanner.enums.TicketStatus;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.exceptions.InvalidNoteTextException;
import com.revature.admin.TravelPlanner.exceptions.NoteNotFoundException;
import com.revature.admin.TravelPlanner.models.Note;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    Logger log = LoggerFactory.getLogger(NoteService.class);

    private NoteDAO noteDAO;
    private SupportTicketDAO ticketDAO;

    public NoteService(NoteDAO noteDAO, SupportTicketDAO ticketDAO) {
        this.noteDAO = noteDAO;
        this.ticketDAO = ticketDAO;
    }

    public Note updateNoteText(int noteId, String updatedText) throws CustomException {
        log.debug("Method 'updateNoteText' invoked with noteId: {}, updatedText: {}", noteId,updatedText);
        Note note = noteDAO
                .findById(noteId)
                .orElseThrow(()->new NoteNotFoundException(noteId));

        if(updatedText == null || updatedText.isEmpty()){
            throw new InvalidNoteTextException();
        }

        note.setText(updatedText);

        noteDAO.save(note);

        log.debug("Method 'updateNoteText' returning: {}",note.toString());
        return note;
    }

    public String deleteNote(int noteId) throws CustomException {
        log.debug("Method 'deleteNote' invoked with noteId: {}", noteId);
        Note note = noteDAO
                .findById(noteId)
                .orElseThrow(()->new NoteNotFoundException(noteId));

        noteDAO.delete(note);
        // Set the ticket status back to Pending when its note is deleted
        SupportTicket ticket = note.getSupportTicket();
        ticket.setStatus(TicketStatus.PENDING);
        ticketDAO.save(ticket);

        log.debug("Method 'deleteNote' completed");
        log.info("Note with ID: {} was deleted, and its related ticket is set back to Pending",noteId);
        return "Note with ID#"+noteId+ " was deleted, and its related ticket is set back to Pending";
    }

    public Note getBySupportTicketId(int ticketId){
        log.debug("Method 'getBySupportTicketId' invoked with ticketId: {}", ticketId);

        Note note =  noteDAO.findBySupportTicketSupportTicketId(ticketId)
                .orElse(null);

        if(note != null)
            log.debug("Method 'getBySupportTicketId' returning: {}", note.toString());
        else
            log.warn("Method 'getBySupportTicketId' returning null");

        return note;
    }


}