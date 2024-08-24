package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.AdminDAO;
import com.revature.admin.TravelPlanner.DAOs.NoteDAO;
import com.revature.admin.TravelPlanner.DAOs.SupportTicketDAO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingSupportTicketDTO;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.InvalidStatusException;
import com.revature.admin.TravelPlanner.exceptions.SupportTicketNotFoundException;
import com.revature.admin.TravelPlanner.mappers.OutgoingSupportTicketMapper;
import com.revature.admin.TravelPlanner.mappers.StatusMapper;
import com.revature.admin.TravelPlanner.mappers.TypeMapper;
import com.revature.admin.TravelPlanner.models.Note;
import com.revature.admin.TravelPlanner.models.SupportTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class SupportTicketService {

    //Model Variable
    private SupportTicketDAO stDao;
    private AdminDAO aDao;
    private NoteDAO nDao;

    //Mappers
    private OutgoingSupportTicketMapper ticketMapper;
    private TypeMapper mapperType;
    private StatusMapper mapperStatus;

    //Mappers need
    //Constructor
    @Autowired
    public SupportTicketService(SupportTicketDAO stDao, AdminDAO aDao, NoteDAO nDao, OutgoingSupportTicketMapper mapperAdmin,
                                TypeMapper mapperType, StatusMapper mapperStatus) {
        this.stDao = stDao;
        this.aDao = aDao;
        this.nDao = nDao;
        this.ticketMapper = mapperAdmin;
        this.mapperType = mapperType;
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

    //Method to return all tickets assigned to an admin
    public List<OutgoingSupportTicketDTO> getAllToAdminId(int id) throws AdminNotFoundException,
            SupportTicketNotFoundException{

        //Check if Admin exists
        if (!(aDao.existsById(id))) {
            throw new AdminNotFoundException(id);
        }

        //Instantiate Lists
        List<Note> nl = nDao.findAllByAdminAdminId(id);
        List<SupportTicket> stl = new ArrayList<SupportTicket>();
        List<OutgoingSupportTicketDTO> returnList = new ArrayList<OutgoingSupportTicketDTO>();

        //Find SupportTickets that are assigned to Admin
        for (Note n: nl) {

            Optional<SupportTicket> optST = stDao.findById(n.getSupportTicket().getSupportTicketId());

            if (optST.isPresent()) {

                stl.add(optST.get());

            } else {
                throw new SupportTicketNotFoundException();

            }

        }

        //Add AdminOutgoingSupportTicketDTO to returnList
        for (SupportTicket st: stl) {

            returnList.add(ticketMapper.toDto(st));

        }

        return returnList;

    }

    //-------------Post Methods------------
    //
    //


    //-------------Patch Methods------------
    //
    //


    //Method to update the Support Ticket Status
    public OutgoingSupportTicketDTO updateStatus(int id, String status) throws SupportTicketNotFoundException, InvalidStatusException {

        Optional<SupportTicket> foundTicket = stDao.findById(id);

        if (foundTicket.isPresent()) {

            SupportTicket updatedTicket = foundTicket.get();
            updatedTicket.setStatus(mapperStatus.tDto(status));
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
