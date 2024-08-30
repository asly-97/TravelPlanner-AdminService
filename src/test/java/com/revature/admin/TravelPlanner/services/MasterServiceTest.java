package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.AdminDAO;
import com.revature.admin.TravelPlanner.DTOs.OutgoingAdminDTO;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.exceptions.EmailAlreadyExistException;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.mappers.AdminMapper;
import com.revature.admin.TravelPlanner.security.PasswordEncoderProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MasterServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private AdminDAO adminDAO;

    @Mock
    private AdminMapper adminMapper;

    @Autowired
    private PasswordEncoderProvider passwordEncoder;

    @InjectMocks
    private MasterService masterService;

    private Admin admin;
    private UUID adminId;
    private String email;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        masterService.setPasswordEncoder(passwordEncoder);
        adminId = UUID.randomUUID();
        email = "test@example.com";
        admin = new Admin();
        admin.setAdminId(adminId);
        admin.setEmail(email);
        admin.setPassword("password");
    }

    @Test
    void getAdminById_AdminExists_ReturnsAdmin() throws CustomException {
        when(adminDAO.findById(adminId)).thenReturn(Optional.of(admin));

        Admin foundAdmin = masterService.getAdminById(adminId);

        assertNotNull(foundAdmin);
        assertEquals(adminId, foundAdmin.getAdminId());
        verify(adminDAO, times(1)).findById(adminId);
    }

    @Test
    void getAdminById_AdminDoesNotExist_ThrowsAdminNotFoundException() {
        when(adminDAO.findById(adminId)).thenReturn(Optional.empty());

        assertThrows(AdminNotFoundException.class, () -> masterService.getAdminById(adminId));

        verify(adminDAO, times(1)).findById(adminId);
    }

    @Test
    void getAdminByEmail_AdminExists_ReturnsAdmin() throws CustomException {
        when(adminDAO.findByEmail(email)).thenReturn(Optional.of(admin));

        Admin foundAdmin = masterService.getAdminByEmail(email);

        assertNotNull(foundAdmin);
        assertEquals(email, foundAdmin.getEmail());
        verify(adminDAO, times(1)).findByEmail(email);
    }

    @Test
    void getAdminByEmail_AdminDoesNotExist_ThrowsAdminNotFoundException() {
        when(adminDAO.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(AdminNotFoundException.class, () -> masterService.getAdminByEmail(email));

        verify(adminDAO, times(1)).findByEmail(email);
    }

    @Test
    void getAllAdmins_ReturnsListOfOutgoingAdminDTOs() {
        List<Admin> adminList = new ArrayList<>();
        adminList.add(admin);

        OutgoingAdminDTO outgoingAdminDTO = new OutgoingAdminDTO();
        when(adminDAO.findAll()).thenReturn(adminList);
        when(adminMapper.toDto(any(Admin.class))).thenReturn(outgoingAdminDTO);

        List<OutgoingAdminDTO> result = masterService.getAllAdmins();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(adminDAO, times(1)).findAll();
        verify(adminMapper, times(1)).toDto(admin);
    }

    @Test
    void createAdmin_AdminAlreadyExists_ThrowsEmailAlreadyExistException() {
        when(adminDAO.findByEmail(email)).thenReturn(Optional.of(admin));

        assertThrows(EmailAlreadyExistException.class, () -> masterService.createAdmin(admin));

        verify(adminDAO, times(1)).findByEmail(email);
    }

    @Test
    void createAdmin_AdminDoesNotExist_CreatesAndReturnsAdmin() throws CustomException {
        when(adminDAO.findByEmail(email)).thenReturn(Optional.empty());
        when(adminDAO.save(admin)).thenReturn(admin);

        Admin createdAdmin = masterService.createAdmin(admin);

        assertNotNull(createdAdmin);
        assertEquals(adminId, createdAdmin.getAdminId());
        verify(adminDAO, times(1)).findByEmail(email);
        verify(adminDAO, times(1)).save(admin);
    }

    @Test
    void deleteAdmin_AdminExists_DeletesAdmin() throws CustomException {
        when(adminDAO.findById(adminId)).thenReturn(Optional.of(admin));

        String result = masterService.deleteAdmin(adminId);

        assertEquals("Admin account was deleted successfully.", result);
        verify(adminDAO, times(1)).findById(adminId);
        verify(adminDAO, times(1)).delete(admin);
    }

    @Test
    void deleteAdmin_AdminDoesNotExist_ThrowsAdminNotFoundException() {
        when(adminDAO.findById(adminId)).thenReturn(Optional.empty());

        assertThrows(AdminNotFoundException.class, () -> masterService.deleteAdmin(adminId));

        verify(adminDAO, times(1)).findById(adminId);
    }
}

