package com.revature.admin.TravelPlanner.DAOs;

import com.revature.admin.TravelPlanner.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteDAO extends JpaRepository<Note, UUID> {

    // Find by support ticket id
    Optional<Note> findBySupportTicketSupportTicketId(UUID ticketId);

}
