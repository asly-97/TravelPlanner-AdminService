package com.revature.admin.TravelPlanner;


import com.revature.admin.TravelPlanner.DAOs.AdminDAO;
import com.revature.admin.TravelPlanner.DAOs.NoteDAO;
import com.revature.admin.TravelPlanner.DAOs.SupportTicketDAO;
import com.revature.admin.TravelPlanner.DAOs.UserDAO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingNoteDTO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingSupportTicketDTO;
import com.revature.admin.TravelPlanner.enums.TicketStatus;
import com.revature.admin.TravelPlanner.enums.TicketType;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.SupportTicketNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.UserNotFoundException;
import com.revature.admin.TravelPlanner.mappers.OutgoingJWTMapper;
import com.revature.admin.TravelPlanner.mappers.OutgoingNoteMapper;
import com.revature.admin.TravelPlanner.mappers.OutgoingSupportTicketMapper;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.models.Note;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import com.revature.admin.TravelPlanner.models.User;
import com.revature.admin.TravelPlanner.security.JwtTokenProvider;
import com.revature.admin.TravelPlanner.services.AuthService;
import com.revature.admin.TravelPlanner.services.SupportTicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class SupportTicketServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private SupportTicketDAO ticketDAO;

    @Mock
    private NoteDAO noteDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private AdminDAO adminDAO;

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

    @Test
    public void testGetAllSupportTickets() {
        //given
        final Date createdAt = new Date(2024, Calendar.AUGUST, 13);
        final UUID adminId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        final UUID ticketId1 = UUID.randomUUID();
        final UUID ticketId2 = UUID.randomUUID();
        final UUID noteId1 = UUID.randomUUID();
        final UUID noteId2 = UUID.randomUUID();

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

        SupportTicket ticket1 = new SupportTicket();
        ticket1.setSupportTicketId(ticketId1);
        ticket1.setUser(user);
        ticket1.setDescription("Description");
        ticket1.setType(TicketType.GENERAL);
        ticket1.setStatus(TicketStatus.PENDING);
        ticket1.setCreatedAt(createdAt);
        ticket1.setResolvedAt(null);

        SupportTicket ticket2 = new SupportTicket();
        ticket2.setSupportTicketId(ticketId2);
        ticket2.setUser(user);
        ticket2.setDescription("Description");
        ticket2.setType(TicketType.GENERAL);
        ticket2.setStatus(TicketStatus.PENDING);
        ticket2.setCreatedAt(createdAt);
        ticket2.setResolvedAt(null);

        List<SupportTicket> tickets = new ArrayList<SupportTicket>();
        tickets.add(ticket1);
        tickets.add(ticket2);

        Note note1 = new Note();
        note1.setNoteId(noteId1);
        note1.setSupportTicket(ticket1);
        note1.setText("Note Text");
        note1.setAdmin(admin);
        note1.setCreatedAt(createdAt);

        Note note2 = new Note();
        note2.setNoteId(noteId2);
        note2.setSupportTicket(ticket2);
        note2.setText("Note Text");
        note2.setAdmin(admin);
        note2.setCreatedAt(createdAt);

        List<Note> notes = new ArrayList<Note>();
        notes.add(note1);
        notes.add(note2);
        admin.setNotes(notes);

        OutgoingNoteDTO outgoingNote1 = new OutgoingNoteDTO();
        outgoingNote1.setNoteId(noteId1);
        outgoingNote1.setAdminId(adminId);
        outgoingNote1.setTicketId(ticketId1);
        outgoingNote1.setText(note1.getText());
        outgoingNote1.setCreatedAt(createdAt);

        OutgoingNoteDTO outgoingNote2 = new OutgoingNoteDTO();
        outgoingNote2.setNoteId(noteId2);
        outgoingNote2.setAdminId(adminId);
        outgoingNote2.setTicketId(ticketId2);
        outgoingNote2.setText(note2.getText());
        outgoingNote2.setCreatedAt(createdAt);

        OutgoingSupportTicketDTO outgoingTicket1 = new OutgoingSupportTicketDTO();
        outgoingTicket1.setSupportTicketId(ticketId1);
        outgoingTicket1.setUserId(userId);
        outgoingTicket1.setDescription(ticket1.getDescription());
        outgoingTicket1.setStatus(ticket1.getStatus());
        outgoingTicket1.setType(ticket1.getType());
        outgoingTicket1.setCreatedAt(ticket1.getCreatedAt());
        outgoingTicket1.setResolvedDate(ticket1.getResolvedAt());
        outgoingTicket1.setNote(outgoingNote1);

        OutgoingSupportTicketDTO outgoingTicket2 = new OutgoingSupportTicketDTO();
        outgoingTicket2.setSupportTicketId(ticketId2);
        outgoingTicket2.setUserId(userId);
        outgoingTicket2.setDescription(ticket2.getDescription());
        outgoingTicket2.setStatus(ticket2.getStatus());
        outgoingTicket2.setType(ticket2.getType());
        outgoingTicket2.setCreatedAt(ticket2.getCreatedAt());
        outgoingTicket2.setResolvedDate(ticket2.getResolvedAt());
        outgoingTicket2.setNote(outgoingNote2);

        List<OutgoingSupportTicketDTO> outgoingTickets = new ArrayList<OutgoingSupportTicketDTO>();
        outgoingTickets.add(outgoingTicket1);
        outgoingTickets.add(outgoingTicket2);

        when(ticketDAO.findAll()).thenReturn(tickets);
        when(noteDAO.findBySupportTicketSupportTicketId(ticketId1)).thenReturn(Optional.of(note1));
        when(noteDAO.findBySupportTicketSupportTicketId(ticketId2)).thenReturn(Optional.of(note2));
        when(noteMapper.toDto(note1)).thenReturn(outgoingNote1);
        when(noteMapper.toDto(note2)).thenReturn(outgoingNote2);
        when(ticketMapper.toDto(ticket1,outgoingNote1)).thenReturn(outgoingTicket1);
        when(ticketMapper.toDto(ticket2,outgoingNote2)).thenReturn(outgoingTicket2);

        //when
        List<OutgoingSupportTicketDTO> returningTickets = supportService.getAlSupportTickets();

        //then
        assertEquals(returningTickets, outgoingTickets);
        verify(ticketDAO, times(1)).findAll();
        verify(noteDAO, times(1)).findBySupportTicketSupportTicketId(ticketId1);
        verify(noteDAO, times(1)).findBySupportTicketSupportTicketId(ticketId2);
        verify(noteMapper, times(1)).toDto(note1);
        verify(noteMapper, times(1)).toDto(note2);
        verify(ticketMapper, times(1)).toDto(ticket1, outgoingNote1);
        verify(ticketMapper, times(1)).toDto(ticket2, outgoingNote2);

    }

    @Test
    public void testGetAllSupportTicketsByUserId() throws Exception{
        //given
        final Date createdAt = new Date(2024, Calendar.AUGUST, 13);
        final UUID adminId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        final UUID ticketId1 = UUID.randomUUID();
        final UUID ticketId2 = UUID.randomUUID();
        final UUID noteId1 = UUID.randomUUID();
        final UUID noteId2 = UUID.randomUUID();

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

        SupportTicket ticket1 = new SupportTicket();
        ticket1.setSupportTicketId(ticketId1);
        ticket1.setUser(user);
        ticket1.setDescription("Description");
        ticket1.setType(TicketType.GENERAL);
        ticket1.setStatus(TicketStatus.PENDING);
        ticket1.setCreatedAt(createdAt);
        ticket1.setResolvedAt(null);

        SupportTicket ticket2 = new SupportTicket();
        ticket2.setSupportTicketId(ticketId2);
        ticket2.setUser(user);
        ticket2.setDescription("Description");
        ticket2.setType(TicketType.GENERAL);
        ticket2.setStatus(TicketStatus.PENDING);
        ticket2.setCreatedAt(createdAt);
        ticket2.setResolvedAt(null);

        List<SupportTicket> tickets = new ArrayList<SupportTicket>();
        tickets.add(ticket1);
        tickets.add(ticket2);

        Note note1 = new Note();
        note1.setNoteId(noteId1);
        note1.setSupportTicket(ticket1);
        note1.setText("Note Text");
        note1.setAdmin(admin);
        note1.setCreatedAt(createdAt);

        Note note2 = new Note();
        note2.setNoteId(noteId2);
        note2.setSupportTicket(ticket2);
        note2.setText("Note Text");
        note2.setAdmin(admin);
        note2.setCreatedAt(createdAt);

        List<Note> notes = new ArrayList<Note>();
        notes.add(note1);
        notes.add(note2);
        admin.setNotes(notes);

        OutgoingNoteDTO outgoingNote1 = new OutgoingNoteDTO();
        outgoingNote1.setNoteId(noteId1);
        outgoingNote1.setAdminId(adminId);
        outgoingNote1.setTicketId(ticketId1);
        outgoingNote1.setText(note1.getText());
        outgoingNote1.setCreatedAt(createdAt);

        OutgoingNoteDTO outgoingNote2 = new OutgoingNoteDTO();
        outgoingNote2.setNoteId(noteId2);
        outgoingNote2.setAdminId(adminId);
        outgoingNote2.setTicketId(ticketId2);
        outgoingNote2.setText(note2.getText());
        outgoingNote2.setCreatedAt(createdAt);

        OutgoingSupportTicketDTO outgoingTicket1 = new OutgoingSupportTicketDTO();
        outgoingTicket1.setSupportTicketId(ticketId1);
        outgoingTicket1.setUserId(userId);
        outgoingTicket1.setDescription(ticket1.getDescription());
        outgoingTicket1.setStatus(ticket1.getStatus());
        outgoingTicket1.setType(ticket1.getType());
        outgoingTicket1.setCreatedAt(ticket1.getCreatedAt());
        outgoingTicket1.setResolvedDate(ticket1.getResolvedAt());
        outgoingTicket1.setNote(outgoingNote1);

        OutgoingSupportTicketDTO outgoingTicket2 = new OutgoingSupportTicketDTO();
        outgoingTicket2.setSupportTicketId(ticketId2);
        outgoingTicket2.setUserId(userId);
        outgoingTicket2.setDescription(ticket2.getDescription());
        outgoingTicket2.setStatus(ticket2.getStatus());
        outgoingTicket2.setType(ticket2.getType());
        outgoingTicket2.setCreatedAt(ticket2.getCreatedAt());
        outgoingTicket2.setResolvedDate(ticket2.getResolvedAt());
        outgoingTicket2.setNote(outgoingNote2);

        List<OutgoingSupportTicketDTO> outgoingTickets = new ArrayList<OutgoingSupportTicketDTO>();
        outgoingTickets.add(outgoingTicket1);
        outgoingTickets.add(outgoingTicket2);

        when(userDAO.findById(userId)).thenReturn(Optional.of(user));
        when(ticketDAO.findAllByUserUserId(userId)).thenReturn(tickets);
        when(noteDAO.findBySupportTicketSupportTicketId(ticketId1)).thenReturn(Optional.of(note1));
        when(noteMapper.toDto(note1)).thenReturn(outgoingNote1);
        when(ticketMapper.toDto(ticket1, outgoingNote1)).thenReturn(outgoingTicket1);
        when(noteDAO.findBySupportTicketSupportTicketId(ticketId2)).thenReturn(Optional.of(note2));
        when(noteMapper.toDto(note2)).thenReturn(outgoingNote2);
        when(ticketMapper.toDto(ticket2, outgoingNote2)).thenReturn(outgoingTicket2);

        //when
        List<OutgoingSupportTicketDTO> returningTickets = supportService.getAllSupportTicketsByUserId(userId);

        //then
        assertEquals(outgoingTickets, returningTickets);
        verify(userDAO, times(1)).findById(userId);
        verify(ticketDAO, times(1)).findAllByUserUserId(userId);
        verify(noteDAO, times(1)).findBySupportTicketSupportTicketId(ticketId1);
        verify(noteMapper, times(1)).toDto(note1);
        verify(ticketMapper, times(1)).toDto(ticket1, outgoingNote1);
        verify(noteDAO, times(1)).findBySupportTicketSupportTicketId(ticketId2);
        verify(noteMapper, times(1)).toDto(note2);
        verify(ticketMapper, times(1)).toDto(ticket2, outgoingNote2);

    }

    @Test
    public void testUserNotFound() {
        //given
        final UUID id = UUID.randomUUID();

        when(userDAO.findById(id)).thenReturn(Optional.empty());

        //when
        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class, () -> supportService.getAllSupportTicketsByUserId(id)
        );

        //then
        assertTrue(thrown.getMessage().contains("User with ID:" + id + " Not Found."));
    }

    //TODO::Fix StackOverflowError
    @Test
    public void testResolve() throws Exception {
//        //given
//        final Date createdAt = new Date(2024, Calendar.AUGUST, 13);
//        final UUID adminId = UUID.randomUUID();
//        final UUID userId = UUID.randomUUID();
//        final UUID ticketId = UUID.randomUUID();
//        final UUID noteId = UUID.randomUUID();
//        final String noteText = "NoteText";
//
//        Admin admin = new Admin();
//        admin.setAdminId(adminId);
//        admin.setFirstName("Bob");
//        admin.setLastName("Smith");
//        admin.setEmail("bobsmith@revature.net");
//        admin.setPassword("password");
//        admin.setCreatedAt(createdAt);
//
//        User user = new User();
//        user.setUserId(userId);
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setEmail("johndoe@revature.net");
//        user.setPassword("password");
//        user.setCreatedAt(createdAt);
//
//        SupportTicket ticket = new SupportTicket();
//        ticket.setSupportTicketId(ticketId);
//        ticket.setUser(user);
//        ticket.setDescription("Description");
//        ticket.setType(TicketType.GENERAL);
//        ticket.setStatus(TicketStatus.PENDING);
//        ticket.setCreatedAt(createdAt);
//        ticket.setResolvedAt(null);
//
//        Note note = new Note();
//        note.setNoteId(noteId);
//        note.setSupportTicket(ticket);
//        note.setText("Note Text");
//        note.setAdmin(admin);
//        note.setCreatedAt(createdAt);
//
//        List<Note> notes = new ArrayList<Note>();
//        notes.add(note);
//        admin.setNotes(notes);
//
//        OutgoingNoteDTO outgoingNote = new OutgoingNoteDTO();
//        outgoingNote.setNoteId(noteId);
//        outgoingNote.setAdminId(adminId);
//        outgoingNote.setTicketId(ticketId);
//        outgoingNote.setText(note.getText());
//        outgoingNote.setCreatedAt(createdAt);
//
//        OutgoingSupportTicketDTO outgoingTicket = new OutgoingSupportTicketDTO();
//        outgoingTicket.setSupportTicketId(ticketId);
//        outgoingTicket.setUserId(userId);
//        outgoingTicket.setDescription(ticket.getDescription());
//        outgoingTicket.setStatus(ticket.getStatus());
//        outgoingTicket.setType(ticket.getType());
//        outgoingTicket.setCreatedAt(ticket.getCreatedAt());
//        outgoingTicket.setResolvedDate(ticket.getResolvedAt());
//        outgoingTicket.setNote(outgoingNote);
//
//        when(ticketDAO.findById(ticketId)).thenReturn(Optional.of(ticket));
//        doReturn(admin).when(authService).getLoggedInAdmin();
//        when(noteDAO.save(note)).thenReturn(note);
//        when(ticketDAO.save(ticket)).thenReturn(ticket);
//        when(noteMapper.toDto(note)).thenReturn(outgoingNote);
//        when(ticketMapper.toDto(ticket, outgoingNote)).thenReturn(outgoingTicket);
//
//        //when
//        OutgoingSupportTicketDTO returningTicket = supportService.resolve(ticketId, noteText);
//
//        //then
//        assertEquals(returningTicket, outgoingTicket);
//        verify(ticketDAO, times(1)).findById(ticketId);
//        verify(authService, times(1)).getLoggedInAdmin();
//        verify(noteDAO, times(1)).save(note);
//        verify(ticketDAO, times(1)).save(ticket);
//        verify(noteMapper, times(1)).toDto(note);
//        verify(ticketMapper, times(1)).toDto(ticket, outgoingNote);

    }

    @Test
    public void testSupportTicketNotFoundResolve() {
        //given
        final UUID ticketId = UUID.randomUUID();
        final String text = "note";

        when(ticketDAO.findById(ticketId)).thenReturn(Optional.empty());

        //when
        SupportTicketNotFoundException thrown = assertThrows(
                SupportTicketNotFoundException.class, () -> supportService.resolve(ticketId, text)
        );

        //then
        assertTrue(thrown.getMessage().contains("Support Ticket with Id: " + ticketId + " Not Found."));
        verify(ticketDAO, times(1)).findById(ticketId);

    }

    @Test
    public void testAdminNotFoundException() throws AdminNotFoundException {
        //given
        final UUID id = UUID.randomUUID();
        final String email = "test@test.com";
        final String text = "Text";

        when(ticketDAO.findById(id)).thenReturn(Optional.of(new SupportTicket()));
        when(authService.getLoggedInAdmin()).thenThrow(AdminNotFoundException.withEmail(email));

        //when
        AdminNotFoundException thrown = assertThrows(
                AdminNotFoundException.class, () -> supportService.resolve(id, text)
        );

        //then
        assertTrue(thrown.getMessage().contains("Admin with email "+email+" Not Found."));
        verify(ticketDAO, times(1)).findById(id);
        verify(authService, times(1)).getLoggedInAdmin();

    }

    @Test
    public void testDelete() {
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

    }

    @Test
    public void testSupportTicketNotFoundDelete() {
        //given
        final UUID id = UUID.randomUUID();

        when(ticketDAO.findById(id)).thenReturn(Optional.empty());

        //when
        SupportTicketNotFoundException thrown = assertThrows(
                SupportTicketNotFoundException.class, () -> supportService.delete(id)
        );

        //then
        assertTrue(thrown.getMessage().contains("Support Ticket with Id: " + id + " Not Found."));
        verify(ticketDAO, times(1)).findById(id);

    }

}//End of SupportTicketServiceTest
