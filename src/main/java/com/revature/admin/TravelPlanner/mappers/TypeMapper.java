package com.revature.admin.TravelPlanner.mappers;

import com.revature.admin.TravelPlanner.enums.TicketType;
import com.sun.jdi.InvalidTypeException;
import org.springframework.stereotype.Component;

@Component
public class TypeMapper {

    public TypeMapper() {
    }

    public TicketType tDto(String type) throws InvalidTypeException {
        try {
            return TicketType.valueOf(type.toUpperCase().trim());

        } catch (IllegalArgumentException e){
            throw new InvalidTypeException(type);

        }
    }
}
