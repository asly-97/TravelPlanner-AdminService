package com.revature.admin.TravelPlanner.mappers;

import com.revature.admin.TravelPlanner.DTOs.OutgoingSupportTicketDTO;
import com.revature.admin.TravelPlanner.enums.TicketStatus;
import com.revature.admin.TravelPlanner.enums.TicketType;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import org.springframework.stereotype.Component;

@Component
public class OutgoingSupportTicketMapper {

    public OutgoingSupportTicketMapper() {
    }

    public OutgoingSupportTicketDTO toDto(SupportTicket supportTicket) {
        int supportTicketId = supportTicket.getSupportTicketId();
        int userId = supportTicket.getUser().getUserId();
        String firstName = supportTicket.getUser().getFirstName();
        String lastName = supportTicket.getUser().getLastName();
        String email = supportTicket.getUser().getEmail();
        String description = supportTicket.getDescription();
        TicketStatus status = supportTicket.getStatus();
        TicketType type = supportTicket.getType();
        long createdDate = supportTicket.getCreatedAt();
        long resolvedDate = supportTicket.getResolvedAt();

        return new OutgoingSupportTicketDTO(
                supportTicketId, userId, firstName, lastName, email, description, status, type, createdDate, resolvedDate
        );
    }
}
