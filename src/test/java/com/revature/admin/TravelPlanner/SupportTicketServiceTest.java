package com.revature.admin.TravelPlanner;

import com.revature.admin.TravelPlanner.DAOs.NoteDAO;
import com.revature.admin.TravelPlanner.DAOs.SupportTicketDAO;
import com.revature.admin.TravelPlanner.DAOs.UserDAO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingNoteDTO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingSupportTicketDTO;
import com.revature.admin.TravelPlanner.enums.TicketStatus;
import com.revature.admin.TravelPlanner.enums.TicketType;
import com.revature.admin.TravelPlanner.exceptions.SupportTicketNotFoundException;
import com.revature.admin.TravelPlanner.mappers.OutgoingNoteMapper;
import com.revature.admin.TravelPlanner.mappers.OutgoingSupportTicketMapper;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.models.Note;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import com.revature.admin.TravelPlanner.models.User;
import com.revature.admin.TravelPlanner.services.AuthService;
import com.revature.admin.TravelPlanner.services.SupportTicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class SupportTicketServiceTest {

    @Mock
    private SupportTicketDAO ticketDAO;

    @Mock
    private NoteDAO noteDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private AuthService authService;

    @Mock
    private OutgoingSupportTicketMapper ticketMapper;

    @Mock
    private OutgoingNoteMapper noteMapper;

    @InjectMocks
    private SupportTicketService supportService;

    @Test
    public void testGetSupportTicketById() throws Exception {
        //given
        final Date createdAt = new Date(2024, Calendar.AUGUST, 13);
        final UUID adminId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        final UUID ticketId = UUID.randomUUID();
        final UUID noteId = UUID.randomUUID();

        Admin admin = new Admin();
        admin.setAdminId(adminId);
        admin.setFirstName("Bob");
        admin.setLastName("Smith");
        admin.setEmail("bobsmith@revature.net");
        admin.setPassword("password");
        admin.setCreatedAt(createdAt);

        User user = new User();
        user.setUserId(userId);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@revature.net");
        user.setPassword("password");
        user.setCreatedAt(createdAt);

        SupportTicket ticket = new SupportTicket();
        ticket.setSupportTicketId(ticketId);
        ticket.setUser(user);
        ticket.setDescription("Description");
        ticket.setType(TicketType.GENERAL);
        ticket.setStatus(TicketStatus.PENDING);
        ticket.setCreatedAt(createdAt);
        ticket.setResolvedAt(null);

        Note note = new Note();
        note.setNoteId(noteId);
        note.setSupportTicket(ticket);
        note.setText("Note Text");
        note.setAdmin(admin);
        note.setCreatedAt(createdAt);

        List<Note> notes = new ArrayList<Note>();
        notes.add(note);
        admin.setNotes(notes);

        OutgoingNoteDTO outgoingNote = new OutgoingNoteDTO();
        outgoingNote.setNoteId(noteId);
        outgoingNote.setAdminId(adminId);
        outgoingNote.setTicketId(ticketId);
        outgoingNote.setText(note.getText());
        outgoingNote.setCreatedAt(createdAt);

        OutgoingSupportTicketDTO outgoingTicket = new OutgoingSupportTicketDTO();
        outgoingTicket.setSupportTicketId(ticketId);
        outgoingTicket.setUserId(userId);
        outgoingTicket.setDescription(ticket.getDescription());
        outgoingTicket.setStatus(ticket.getStatus());
        outgoingTicket.setType(ticket.getType());
        outgoingTicket.setCreatedAt(ticket.getCreatedAt());
        outgoingTicket.setResolvedDate(ticket.getResolvedAt());
        outgoingTicket.setNote(outgoingNote);

        when(ticketDAO.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(noteDAO.findBySupportTicketSupportTicketId(ticketId)).thenReturn(Optional.of(note));
        when(noteMapper.toDto(note)).thenReturn(outgoingNote);
        when(ticketMapper.toDto(ticket,outgoingNote)).thenReturn(outgoingTicket);

        //when
        OutgoingSupportTicketDTO returningTicket = supportService.getSupportTicketById(ticketId);

        //then
        assertEquals(returningTicket, outgoingTicket);
        verify(ticketDAO, times(1)).findById(ticketId);
        verify(noteDAO, times(1)).findBySupportTicketSupportTicketId(ticketId);
        verify(noteMapper, times(1)).toDto(note);
        verify(ticketMapper, times(1)).toDto(ticket, outgoingNote);

    }

    @Test
    public void testSupportTicketNotFoundById() {
        //given
        final UUID id = UUID.randomUUID();

        when(ticketDAO.findById(id)).thenReturn(Optional.empty());

        //when
        SupportTicketNotFoundException thrown = assertThrows(
                SupportTicketNotFoundException.class, () -> supportService.getSupportTicketById(id)
        );

        //then
        assertTrue(thrown.getMessage().contains("Support Ticket with Id: " + id + " Not Found."));
        verify(ticketDAO, times(1)).findById(id);

    }


}//End of SupportTicketServiceTest
