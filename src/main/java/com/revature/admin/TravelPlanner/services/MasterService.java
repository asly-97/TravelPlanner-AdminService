package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DTOs.OutgoingAdminDTO;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.exceptions.EmailAlreadyExistException;
import com.revature.admin.TravelPlanner.models.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@PreAuthorize("hasRole('ROLE_MASTER')")
public class MasterService extends AdminService{
    Logger log = LoggerFactory.getLogger(MasterService.class);

    public Admin getAdminById(int adminId) throws CustomException {
        log.debug("Method 'getAdminById' invoked with adminId: {}", adminId);

        Optional<Admin> admin = adminDAO.findById(adminId);
        if(admin.isPresent()){
            log.debug("Method 'getAdminById' returning: {}", admin.get().toString());
            return admin.get();
        }
        throw new AdminNotFoundException("Admin with id: " + adminId+" was not found.");
    }


    public Admin getAdminByEmail(String email) throws CustomException {
        log.debug("Method 'getAdminByEmail' invoked with email: {}", email);

        Optional<Admin> admin = adminDAO.findByEmail(email);
        if(admin.isPresent()){
            log.debug("Method 'getAdminByEmail' returning: {}", admin.get().toString());
            return admin.get();
        }
        throw new AdminNotFoundException("Admin with email: " + email+" was not found.");
    }

    //Method to return OutgoingAdmins to the controller
    public List<OutgoingAdminDTO> getAllAdmins() {
        log.debug("Method 'getAllAdmins' invoked");

        List<Admin> al = adminDAO.findAll();
        List<OutgoingAdminDTO> returnedList = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (Admin oa : al) {
            sb.append(oa.getAdminId()).append(", ");
            returnedList.add(adminMapper.toDto(oa));
        }

        log.debug("Method 'getAllAdmins' returning OutgoingAdminDTO list with admin_ids: {}", sb.toString());
        return returnedList;

    }

    public Admin createAdmin(Admin newAdmin) throws CustomException{
        log.debug("Method 'createAdmin' invoked with newAdmin: {}",newAdmin);
        if(adminDAO.findByEmail(newAdmin.getEmail()).isPresent()){
            throw new EmailAlreadyExistException("Admin with email: " + newAdmin.getEmail()+" already exists.");
        }

        String encodedPassword = passwordEncoder.encode(newAdmin.getPassword());
        newAdmin.setPassword(encodedPassword);

        Admin admin = adminDAO.save(newAdmin);
        log.debug("Method 'createAdmin' returning: {}",admin);
        return admin;
    }


    public String deleteAdmin(int adminId) throws CustomException{
        log.debug("Method 'deleteAdmin' invoked with adminId: {}",adminId);

        Admin admin = adminDAO.findById(adminId)
                .orElseThrow(()->new AdminNotFoundException(adminId));

        adminDAO.delete(admin);
        log.info("Admin account with id: {} was deleted successfully",adminId);
        log.debug("Method 'deleteAdmin' completed");
        return "Admin account was deleted successfully.";
    }
}
