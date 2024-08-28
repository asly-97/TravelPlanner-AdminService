package com.revature.admin.TravelPlanner.mappers;

import com.revature.admin.TravelPlanner.DTOs.OutgoingNoteDTO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingSupportTicketDTO;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import org.springframework.stereotype.Component;

@Component
public class OutgoingSupportTicketMapper {

    public OutgoingSupportTicketMapper() {
    }

    public OutgoingSupportTicketDTO toDto(SupportTicket ticket, OutgoingNoteDTO noteDTO) {
        return new OutgoingSupportTicketDTO(
                ticket.getSupportTicketId(),
                ticket.getUser().getUserId(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getType(),
                ticket.getCreatedAt(),
                ticket.getResolvedAt(),
                noteDTO
        );
    }
}
