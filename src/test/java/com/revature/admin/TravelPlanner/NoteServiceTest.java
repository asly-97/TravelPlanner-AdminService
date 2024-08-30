package com.revature.admin.TravelPlanner;

import com.revature.admin.TravelPlanner.DAOs.NoteDAO;
import com.revature.admin.TravelPlanner.DAOs.SupportTicketDAO;
import com.revature.admin.TravelPlanner.enums.TicketStatus;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.exceptions.InvalidNoteTextException;
import com.revature.admin.TravelPlanner.exceptions.NoteNotFoundException;
import com.revature.admin.TravelPlanner.models.Note;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import com.revature.admin.TravelPlanner.services.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    @Mock
    private NoteDAO noteDAO;

    @Mock
    private SupportTicketDAO ticketDAO;

    @InjectMocks
    private NoteService noteService;

    private UUID noteId;
    private UUID ticketId;
    private Note note;
    private SupportTicket ticket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        noteId = UUID.randomUUID();
        ticketId = UUID.randomUUID();

        ticket = new SupportTicket();
        ticket.setSupportTicketId(ticketId);
        ticket.setStatus(TicketStatus.RESOLVED);

        note = new Note();
        note.setNoteId(noteId);
        note.setText("Initial Text");
        note.setSupportTicket(ticket);
    }

    @Test
    void updateNoteText_ValidText_UpdatesAndReturnsNote() throws CustomException {
        String updatedText = "Updated Text";

        when(noteDAO.findById(noteId)).thenReturn(Optional.of(note));

        Note updatedNote = noteService.updateNoteText(noteId, updatedText);

        assertNotNull(updatedNote);
        assertEquals(updatedText, updatedNote.getText());
        verify(noteDAO, times(1)).findById(noteId);
        verify(noteDAO, times(1)).save(note);
    }

    @Test
    void updateNoteText_NoteNotFound_ThrowsNoteNotFoundException() {
        when(noteDAO.findById(noteId)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> noteService.updateNoteText(noteId, "Updated Text"));

        verify(noteDAO, times(1)).findById(noteId);
    }

    @Test
    void updateNoteText_InvalidText_ThrowsInvalidNoteTextException() {
        when(noteDAO.findById(noteId)).thenReturn(Optional.of(note));

        assertThrows(InvalidNoteTextException.class, () -> noteService.updateNoteText(noteId, ""));

        verify(noteDAO, times(1)).findById(noteId);
        verify(noteDAO, never()).save(any(Note.class));
    }

    @Test
    void deleteNote_NoteExists_DeletesNoteAndUpdatesTicket() throws CustomException {
        when(noteDAO.findById(noteId)).thenReturn(Optional.of(note));

        String result = noteService.deleteNote(noteId);

        assertEquals("Note with ID#" + noteId + " was deleted, and its related ticket is set back to Pending", result);
        assertEquals(TicketStatus.PENDING, ticket.getStatus());
        verify(noteDAO, times(1)).findById(noteId);
        verify(noteDAO, times(1)).delete(note);
        verify(ticketDAO, times(1)).save(ticket);
    }

    @Test
    void deleteNote_NoteNotFound_ThrowsNoteNotFoundException() {
        when(noteDAO.findById(noteId)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> noteService.deleteNote(noteId));

        verify(noteDAO, times(1)).findById(noteId);
        verify(noteDAO, never()).delete(any(Note.class));
        verify(ticketDAO, never()).save(any(SupportTicket.class));
    }

    @Test
    void getBySupportTicketId_TicketExists_ReturnsNote() {
        when(noteDAO.findBySupportTicketSupportTicketId(ticketId)).thenReturn(Optional.of(note));

        Note foundNote = noteService.getBySupportTicketId(ticketId);

        assertNotNull(foundNote);
        assertEquals(noteId, foundNote.getNoteId());
        verify(noteDAO, times(1)).findBySupportTicketSupportTicketId(ticketId);
    }

    @Test
    void getBySupportTicketId_TicketDoesNotExist_ReturnsNull() {
        when(noteDAO.findBySupportTicketSupportTicketId(ticketId)).thenReturn(Optional.empty());

        Note foundNote = noteService.getBySupportTicketId(ticketId);

        assertNull(foundNote);
        verify(noteDAO, times(1)).findBySupportTicketSupportTicketId(ticketId);
    }
}
