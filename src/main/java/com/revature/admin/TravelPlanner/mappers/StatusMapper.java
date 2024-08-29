package com.revature.admin.TravelPlanner.mappers;

import com.revature.admin.TravelPlanner.enums.TicketStatus;
import com.revature.admin.TravelPlanner.exceptions.InvalidStatusException;
import org.springframework.stereotype.Component;

@Component
public class StatusMapper {

    public StatusMapper() {
    }

    public TicketStatus toDto(String status) throws InvalidStatusException {
        try {
            return TicketStatus.valueOf(status.toUpperCase().trim());

        } catch (IllegalArgumentException e){
            throw new InvalidStatusException(status);

        }
    }
}
