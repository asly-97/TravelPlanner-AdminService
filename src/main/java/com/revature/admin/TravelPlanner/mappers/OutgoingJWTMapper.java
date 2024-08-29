package com.revature.admin.TravelPlanner.mappers;

import com.revature.admin.TravelPlanner.DTOs.OutgoingJwtDTO;
import com.revature.admin.TravelPlanner.models.Admin;
import org.springframework.stereotype.Component;

@Component
public class OutgoingJWTMapper {

    public OutgoingJWTMapper() {
    }

    public OutgoingJwtDTO toDto(Admin admin, String token){
        return new OutgoingJwtDTO(
                admin.getAdminId(),
                admin.getFirstName(),
                admin.getLastName(),
                admin.getEmail(),
                admin.isMaster(),
                token
        );
    }

}
