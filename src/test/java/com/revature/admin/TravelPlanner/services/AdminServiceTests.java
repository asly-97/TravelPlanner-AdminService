package com.revature.admin.TravelPlanner.services;

import com.revature.admin.TravelPlanner.DAOs.AdminDAO;
import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.exceptions.EmailAlreadyExistException;
import com.revature.admin.TravelPlanner.mappers.AdminMapper;
import com.revature.admin.TravelPlanner.models.Admin;
import com.revature.admin.TravelPlanner.security.PasswordEncoderProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {

    @Mock
    private AdminDAO adminDAO;

    @Spy
    private AdminMapper adminMapper;

    @Spy
    private PasswordEncoderProvider passwordEncoder;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AdminService adminService;

    private Admin admin;
    private UUID adminId1;
    private UUID adminId2;

    @BeforeEach
    public void setUp() {
        admin = new Admin();
        adminId1 = UUID.randomUUID();
        adminId2 = UUID.randomUUID();
        admin.setAdminId(adminId1);
        admin.setFirstName("John");
        admin.setLastName("Doe");
        admin.setEmail("john.doe@example.com");
    }

    @Test
    public void testGetAdminByIdExist() throws Exception {
        // Arrange
        when(adminDAO.findById(adminId1)).thenReturn(Optional.of(admin));

        // Act
        Admin result = adminService.findById(adminId1);

        // Assert
        assertEquals(admin.getAdminId(), result.getAdminId());
    }
    @Test
    public void testGetAdminByIdNull() {
        // Arrange
        when(adminDAO.findById(adminId2)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AdminNotFoundException.class, () -> adminService.findById(adminId2));
    }

    @Test
    public void testGetAdminByEmailNull() {
        // Arrange
        String testEmail = "notfound@example.com";
        when(adminDAO.findByEmail(testEmail)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AdminNotFoundException.class, () -> adminService.findByEmail(testEmail));
    }

    @Test
    public void testGetAllAdmins() {
        // Arrange
        List<Admin> adminList = Arrays.asList(admin);
        //OutgoingAdminDTO outgoingAdminDTO = new OutgoingAdminDTO();

        when(adminDAO.findAll()).thenReturn(adminList);
        //when(adminMapper.toDto(admin)).thenReturn(outgoingAdminDTO);

        // Act
        List<Admin> result = adminService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(admin.getAdminId(), result.get(0).getAdminId());
    }

    @Test
    public void testUpdateLoggedInAdmin() throws CustomException {
        // Arrange
        Map<String, String> expectedUpdateData = new HashMap<>();
        expectedUpdateData.put("firstName", "Jane");
        expectedUpdateData.put("lastName", "Doe");
        expectedUpdateData.put("email", "jane.doe.new@example.com");

        when(authService.getLoggedInAdmin()).thenReturn(admin);
        when(adminDAO.save(admin)).thenReturn(admin);
        when(adminDAO.findByEmail(expectedUpdateData.get("email"))).thenReturn(Optional.empty());

        // Act
        Admin result = adminService.updateLoggedInAdmin(expectedUpdateData);

        // Assert
        assertEquals(expectedUpdateData.get("firstName"), result.getFirstName());
        assertEquals(expectedUpdateData.get("lastName"), result.getLastName());
        assertEquals(expectedUpdateData.get("email"), result.getEmail());
    }

    @Test
    public void testUpdateLoggedInAdminEmailAlreadyExists() throws CustomException{
        // Arrange
        Admin anotherAdmin = new Admin();
        anotherAdmin.setAdminId(UUID.randomUUID());
        anotherAdmin.setEmail("jane.doe@example.com");

        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("email", "jane.doe@example.com");

        when(authService.getLoggedInAdmin()).thenReturn(admin);
        when(adminDAO.findByEmail(anotherAdmin.getEmail())).thenReturn(Optional.of(anotherAdmin));

        // Act & Assert
        assertThrows(EmailAlreadyExistException.class, () -> adminService.updateLoggedInAdmin(updatedData));
    }





}
