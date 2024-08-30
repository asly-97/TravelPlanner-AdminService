package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.AdminDAO;
import com.revature.admin.TravelPlanner.controllers.AuthController;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.exceptions.EmailAlreadyExistException;
import com.revature.admin.TravelPlanner.mappers.AdminMapper;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.security.PasswordEncoderProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
public class AdminService {


    protected AuthService authService;

    //DAOs

    protected AdminDAO adminDAO;

    //Mapper

    protected AdminMapper adminMapper;


    protected PasswordEncoderProvider passwordEncoder;

    @Autowired
    public AdminService(AuthService authService, AdminDAO adminDAO, AdminMapper adminMapper, PasswordEncoderProvider passwordEncoder) {
        this.authService = authService;
        this.adminDAO = adminDAO;
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin updateLoggedInAdmin(Map<String, String> newAdmin) throws CustomException {
        Admin admin = authService.getLoggedInAdmin();

        if(newAdmin.containsKey("firstName")) {
            admin.setFirstName(newAdmin.get("firstName"));
        }
        if(newAdmin.containsKey("lastName")) {
            admin.setLastName(newAdmin.get("lastName"));
        }
        if(newAdmin.containsKey("email")) {
            Optional<Admin> admin2 = adminDAO.findByEmail(newAdmin.get("email"));
            if(admin2.isPresent()){

                if(admin2.get().getAdminId() != authService.getLoggedInAdmin().getAdminId()){

                    throw new EmailAlreadyExistException("Another admin already uses this email address");
                }
            }
            admin.setEmail(newAdmin.get("email"));
        }
        if(newAdmin.containsKey("password")) {
            admin.setFirstName(newAdmin.get("password"));
        }

        return adminDAO.save(admin);
    }

    public Admin findById(UUID id) throws AdminNotFoundException{
        return adminDAO.findById(id).orElseThrow(()->new AdminNotFoundException(id));
    }
    public Admin findByEmail(String email) throws AdminNotFoundException{
        return adminDAO.findByEmail(email).orElseThrow(() -> AdminNotFoundException.withEmail(email));
    }

    public List<Admin> findAll(){
        return adminDAO.findAll();
    }
}




