package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.AdminDAO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingAdminDTO;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.exceptions.EmailAlreadyExistException;
import com.revature.admin.TravelPlanner.mappers.OutgoingJWTMapper;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.security.PasswordEncoderProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class AdminService {

    //DAOs
    private AdminDAO aDao;

    //Mapper
    private OutgoingJWTMapper jwtMapper;

    /* TODO: uncomment when admin authcontroller is made
    @Autowired
    AdminAuthController authController;
    */

    @Autowired
    private PasswordEncoderProvider passwordEncoder;

    @Autowired
    public AdminService(AdminDAO aDao, OutgoingJWTMapper jwtMapper) {
        this.aDao = aDao;
        this.jwtMapper = jwtMapper;
    }

    //Service Methods

    public Admin getAdminById(int adminId) throws CustomException {
        Optional<Admin> admin = aDao.findById(adminId);
        if(admin.isPresent()){
            return admin.get();
        }
        throw new AdminNotFoundException("Admin with id: " + adminId+" was not found.");
    }


    public Admin getAdminByEmail(String email) throws CustomException {
        Optional<Admin> admin = aDao.findByEmail(email);
        if(admin.isPresent()){
            return admin.get();
        }
        throw new AdminNotFoundException("Admin with email: " + email+" was not found.");
    }

    //TODO::Refractor with JWTMapper
    //Method to return OutgoingAdmins to the controller
    public List<OutgoingAdminDTO> getAllAdmins() {
//
//        List<Admin> al = aDao.findAll();
//        List<OutgoingAdminDTO> returnedList = new ArrayList<OutgoingAdminDTO>();
//
//        for (Admin oa : al) {
//            returnedList.add(am.toDto(oa));
//        }
//
//        return returnedList;
    return null;
    }

    public Admin updateAdminById(int adminId, Map<String,String> newAdmin) throws CustomException {
        Admin admin = getAdminById(adminId);

        if(newAdmin.containsKey("firstName")) {
            admin.setFirstName(newAdmin.get("firstName"));
        }
        if(newAdmin.containsKey("lastName")) {
            admin.setLastName(newAdmin.get("lastName"));
        }
        if(newAdmin.containsKey("email")) {
            Optional<Admin> admin2 = aDao.findByEmail(newAdmin.get("email"));
            if(admin2.isPresent()){
                if(admin2.get().getAdminId() != 0){ // TODO: replace 0 with authController.getAuthenticatedAdmin().getAdminId
                    throw new EmailAlreadyExistException("Another admin already uses this email address");
                }
            }
            admin.setEmail(newAdmin.get("email"));
        }
        if(newAdmin.containsKey("password")) {
            admin.setFirstName(newAdmin.get("password"));
        }

        return aDao.save(admin);
    }

    public Admin createAdmin(Admin newAdmin) throws CustomException{
        if(aDao.findByEmail(newAdmin.getEmail()).isPresent()){
            throw new EmailAlreadyExistException("Admin with email: " + newAdmin.getEmail()+" already exists.");
        }

        String encodedPassword = passwordEncoder.encode(newAdmin.getPassword());
        newAdmin.setPassword(encodedPassword);

        return aDao.save(newAdmin);
    }


}




