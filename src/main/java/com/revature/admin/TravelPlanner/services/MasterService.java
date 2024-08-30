package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.AdminDAO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingAdminDTO;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.exceptions.EmailAlreadyExistException;
import com.revature.admin.TravelPlanner.mappers.AdminMapper;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.security.PasswordEncoderProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@PreAuthorize("hasRole('ROLE_MASTER')")
public class MasterService extends AdminService{

    protected AuthService authService;

    //DAOs

    protected AdminDAO adminDAO;

    //Mapper

    protected AdminMapper adminMapper;


    protected PasswordEncoderProvider passwordEncoder;

    @Autowired
    public MasterService(AuthService authService, AdminDAO adminDAO, AdminMapper adminMapper, PasswordEncoderProvider passwordEncoder) {
        super(authService, adminDAO, adminMapper, passwordEncoder);
    }

    public void setPasswordEncoder(PasswordEncoderProvider passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Admin getAdminById(UUID adminId) throws CustomException {
        Optional<Admin> admin = adminDAO.findById(adminId);
        if(admin.isPresent()){
            return admin.get();
        }
        throw new AdminNotFoundException("Admin with id: " + adminId+" was not found.");
    }


    public Admin getAdminByEmail(String email) throws CustomException {
        Optional<Admin> admin = adminDAO.findByEmail(email);
        if(admin.isPresent()){
            return admin.get();
        }
        throw new AdminNotFoundException("Admin with email: " + email+" was not found.");
    }

    //Method to return OutgoingAdmins to the controller
    public List<OutgoingAdminDTO> getAllAdmins() {

        List<Admin> al = adminDAO.findAll();
        List<OutgoingAdminDTO> returnedList = new ArrayList<>();

        for (Admin oa : al) {
            returnedList.add(adminMapper.toDto(oa));
        }

        return returnedList;

    }

    public Admin createAdmin(Admin newAdmin) throws CustomException{
        if(adminDAO.findByEmail(newAdmin.getEmail()).isPresent()){
            throw new EmailAlreadyExistException("Admin with email: " + newAdmin.getEmail()+" already exists.");
        }

        String encodedPassword = passwordEncoder.encode(newAdmin.getPassword());
        newAdmin.setPassword(encodedPassword);

        return adminDAO.save(newAdmin);
    }


    public String deleteAdmin(UUID adminId) throws CustomException{
        Admin admin = adminDAO.findById(adminId)
                .orElseThrow(()->new AdminNotFoundException(adminId));
        adminDAO.delete(admin);
        return "Admin account was deleted successfully.";
    }
}
