package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.AdminDAO;
import com.revature.admin.TravelPlanner.DAOs.NoteDAO;
import com.revature.admin.TravelPlanner.DAOs.SupportTicketDAO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingSupportTicketDTO;
import com.revature.admin.TravelPlanner.exceptions.InvalidStatusException;
import com.revature.admin.TravelPlanner.exceptions.SupportTicketNotFoundException;
import com.revature.admin.TravelPlanner.mappers.OutgoingSupportTicketMapper;
import com.revature.admin.TravelPlanner.mappers.StatusMapper;
import com.revature.admin.TravelPlanner.mappers.TypeMapper;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class SupportTicketService {

    //Model Variable
    private SupportTicketDAO stDao;

    //Mappers
    private OutgoingSupportTicketMapper ticketMapper;
    private StatusMapper mapperStatus;

    //Constructor
    @Autowired
    public SupportTicketService(SupportTicketDAO stDao, AdminDAO aDao, NoteDAO nDao, OutgoingSupportTicketMapper mapperAdmin,
                                TypeMapper mapperType, StatusMapper mapperStatus) {
        this.stDao = stDao;
        this.ticketMapper = mapperAdmin;
        this.mapperStatus = mapperStatus;
    }


    //-------------Get Methods------------
    //
    //

    //Method to return a SupportTicket by its id with the associated User using userId and email
    public OutgoingSupportTicketDTO getSupportTicketById(int id) throws SupportTicketNotFoundException {

        Optional<SupportTicket> st = stDao.findById(id);

        if (st.isPresent()) {

             return ticketMapper.toDto(st.get());

        } else {
            throw new SupportTicketNotFoundException(id);

        }

    }

    //Methods to return all SupportTickets for Admins
    public List<OutgoingSupportTicketDTO> getAlSupportTickets() {

        //Instantiate Lists
        List<SupportTicket> stl = stDao.findAll();
        List<OutgoingSupportTicketDTO> returnList = new ArrayList<>();

        for (SupportTicket st: stl) {
            returnList.add(ticketMapper.toDto(st));

        }

        return returnList;

    }

    //-------------Patch Methods------------
    //
    //


    //Method to resolve a Support Ticket
    public OutgoingSupportTicketDTO resolve(int id, String status) throws SupportTicketNotFoundException, InvalidStatusException {

        Optional<SupportTicket> foundTicket = stDao.findById(id);

        if (foundTicket.isPresent()) {

            SupportTicket updatedTicket = foundTicket.get();
            updatedTicket.setStatus(mapperStatus.toDto(status));
            updatedTicket.setResolvedAt(Instant.now().toEpochMilli());
            stDao.save(updatedTicket);
            return ticketMapper.toDto(updatedTicket);

        } else {
            throw new SupportTicketNotFoundException(id);

        }

    }


    //-------------Delete Methods------------
    //
    //

    //Method to delete a Support Ticket from the Database
    public OutgoingSupportTicketDTO delete(int id) throws SupportTicketNotFoundException {

        Optional<SupportTicket> toDeleteTicket = stDao.findById(id);

        if (toDeleteTicket.isPresent()) {

            stDao.delete(toDeleteTicket.get());
            return ticketMapper.toDto(toDeleteTicket.get());

        } else {
            throw new SupportTicketNotFoundException(id);

        }

    }

}//End of SupportTicketService
