package com.revature.admin.TravelPlanner.mappers;

import com.revature.admin.TravelPlanner.DTOs.OutgoingNoteDTO;
import com.revature.admin.TravelPlanner.models.Note;
import org.springframework.stereotype.Component;

@Component
public class OutgoingNoteMapper {

    public OutgoingNoteMapper() {
    }

    public OutgoingNoteDTO toDto(Note note) {
        OutgoingNoteDTO returnNote = new OutgoingNoteDTO(
                note.getNoteId(),
                note.getAdmin().getAdminId(),
                note.getSupportTicket().getSupportTicketId(),
                note.getSupportTicket().getUser().getUserId(),
                note.getSupportTicket().getStatus(),
                note.getSupportTicket().getType(),
                note.getSupportTicket().getDescription(),
                note.getSupportTicket().getCreatedAt(),
                note.getSupportTicket().getResolvedAt()
        );

        return returnNote;
    }
}
