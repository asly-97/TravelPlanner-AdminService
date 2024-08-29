package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.NoteDAO;
import com.revature.admin.TravelPlanner.DAOs.SupportTicketDAO;
import com.revature.admin.TravelPlanner.enums.TicketStatus;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.exceptions.InvalidNoteTextException;
import com.revature.admin.TravelPlanner.exceptions.NoteNotFoundException;
import com.revature.admin.TravelPlanner.models.Note;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NoteService {
    private NoteDAO noteDAO;
    private SupportTicketDAO ticketDAO;

    public NoteService(NoteDAO noteDAO, SupportTicketDAO ticketDAO) {
        this.noteDAO = noteDAO;
        this.ticketDAO = ticketDAO;
    }

    public Note updateNoteText(UUID noteId, String updatedText) throws CustomException {
        Note note = noteDAO
                .findById(noteId)
                .orElseThrow(()->new NoteNotFoundException(noteId));

        if(updatedText == null || updatedText.isEmpty()){
            throw new InvalidNoteTextException();
        }

        note.setText(updatedText);

        noteDAO.save(note);

        return note;
    }

    public String deleteNote(UUID noteId) throws CustomException {
        Note note = noteDAO
                .findById(noteId)
                .orElseThrow(()->new NoteNotFoundException(noteId));

        noteDAO.delete(note);
        // Set the ticket status back to Pending when its note is deleted
        SupportTicket ticket = note.getSupportTicket();
        ticket.setStatus(TicketStatus.PENDING);
        ticketDAO.save(ticket);

        return "Note with ID#"+noteId+ " was deleted, and its related ticket is set back to Pending";
    }

    public Note getBySupportTicketId(UUID ticketId){
        return noteDAO
                .findBySupportTicketSupportTicketId(ticketId)
                .orElse(null);
    }


}