package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.UserDAO;
import com.revature.admin.TravelPlanner.exceptions.UserNotFoundException;
import com.revature.admin.TravelPlanner.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public User findById(UUID userId) throws UserNotFoundException{
        return userDAO.findById(userId).orElseThrow(UserNotFoundException::new);
    }

}
