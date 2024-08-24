package com.revature.admin.TravelPlanner.mappers;

import com.revature.admin.TravelPlanner.DTOs.OutgoingAdminDTO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingJwtDTO;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.models.Note;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class AdminMapper {

    public OutgoingAdminDTO toDto(Admin admin) {
        List<String> notes = admin.getNotes().stream().map(Note::getText).collect(toList());

        return new OutgoingAdminDTO(
                admin.getAdminId(),
                admin.isMaster(),
                admin.getFirstName(),
                admin.getLastName(),
                admin.getEmail(),
                notes
        );
    }

    public OutgoingJwtDTO toDto(Admin admin,String token){
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
