package com.revature.admin.TravelPlanner.DAOs;

import com.revature.admin.TravelPlanner.DTOs.OutgoingSupportTicketDTO;
import com.revature.admin.TravelPlanner.enums.TicketType;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketDAO extends JpaRepository<SupportTicket, Integer> {

    List<OutgoingSupportTicketDTO> findAllByType(TicketType type);

}
