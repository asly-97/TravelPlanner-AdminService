package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.NoteDAO;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.exceptions.InvalidNoteTextException;
import com.revature.admin.TravelPlanner.exceptions.NoteNotFoundException;
import com.revature.admin.TravelPlanner.models.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    private NoteDAO noteDAO;

    @Autowired
    public NoteService(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    public Note updateNoteText(int noteId, String updatedText) throws CustomException {
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

    public String deleteNote(int noteId) throws CustomException {
        Note note = noteDAO
                .findById(noteId)
                .orElseThrow(()->new NoteNotFoundException(noteId));

        noteDAO.delete(note);

        return "Note with ID#"+noteId+ " was deleted successfully";
    }

    public Note getBySupportTicketId(int ticketId){
        return noteDAO
                .findBySupportTicketSupportTicketId(ticketId)
                .orElse(null);
    }


}