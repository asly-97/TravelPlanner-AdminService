package com.revature.admin.TravelPlanner;

import com.revature.admin.TravelPlanner.DAOs.UserDAO;
import com.revature.admin.TravelPlanner.exceptions.UserNotFoundException;
import com.revature.admin.TravelPlanner.models.User;
import com.revature.admin.TravelPlanner.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        user = new User();
        user.setUserId(userId);
    }

    @Test
    void findById_UserExists_ReturnsUser() throws UserNotFoundException {
        when(userDAO.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(userId);

        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getUserId());
        verify(userDAO, times(1)).findById(userId);
    }

    @Test
    void findById_UserDoesNotExist_ThrowsUserNotFoundException() {
        when(userDAO.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));

        verify(userDAO, times(1)).findById(userId);
    }
}
