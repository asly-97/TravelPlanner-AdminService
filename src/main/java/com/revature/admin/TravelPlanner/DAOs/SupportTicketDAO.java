package com.revature.admin.TravelPlanner.DAOs;

import com.revature.admin.TravelPlanner.DTOs.OutgoingSupportTicketDTO;
import com.revature.admin.TravelPlanner.enums.TicketType;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SupportTicketDAO extends JpaRepository<SupportTicket, UUID> {

    List<SupportTicket> findAllByType(TicketType type);

}
