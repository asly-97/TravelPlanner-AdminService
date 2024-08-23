package com.revature.admin.TravelPlanner.mappers;

import com.revature.admin.TravelPlanner.DTOs.OutgoingAdminDTO;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.models.Note;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class AdminMapper {

    public OutgoingAdminDTO toDto(Admin admin) {
        String lastName = admin.getLastName();
        String email = admin.getEmail();

        List<String> notes = admin.getNotes().stream().map(Note::getText).collect(toList());

        return new OutgoingAdminDTO(lastName, email, notes);
    }

}
