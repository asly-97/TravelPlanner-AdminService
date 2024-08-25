package com.revature.admin.TravelPlanner.DAOs;

import com.revature.admin.TravelPlanner.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteDAO extends JpaRepository<Note, Integer> {

    // Find by support ticket id
    Optional<Note> findBySupportTicketSupportTicketId(int ticketId);

}
