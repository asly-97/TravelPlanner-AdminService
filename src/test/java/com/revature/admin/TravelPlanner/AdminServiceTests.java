//package com.revature.admin.TravelPlanner;
//
//import com.revature.admin.TravelPlanner.DAOs.AdminDAO;
//import com.revature.admin.TravelPlanner.DTOs.OutgoingAdminDTO;
//import com.revature.admin.TravelPlanner.exceptions.AdminNotFoundException;
//import com.revature.admin.TravelPlanner.exceptions.CustomException;
//import com.revature.admin.TravelPlanner.exceptions.EmailAlreadyExistException;
//import com.revature.admin.TravelPlanner.mappers.AdminMapper;
//import com.revature.admin.TravelPlanner.models.Admin;
//import com.revature.admin.TravelPlanner.security.PasswordEncoderProvider;
//import com.revature.admin.TravelPlanner.services.AdminService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class AdminServiceTests {
//
//    @Mock
//    private AdminDAO adminDAO;
//
//    @Mock
//    private AdminMapper adminMapper;
//
//    @Mock
//    private PasswordEncoderProvider passwordEncoder;
//
//    @InjectMocks
//    private AdminService adminService;
//
//    private Admin admin;
//
//    @BeforeEach
//    public void setUp() {
//        admin = new Admin();
//        admin.setAdminId(1);
//        admin.setFirstName("John");
//        admin.setLastName("Doe");
//        admin.setEmail("john.doe@example.com");
//    }
//
//    @Test
//    public void testGetAdminByIdExist() throws Exception {
//        // Arrange
//        when(adminDAO.findById(1)).thenReturn(Optional.of(admin));
//
//        // Act
//        Admin result = adminService.getAdminById(1);
//
//        // Assert
//        assertEquals(admin, result);
//    }
//    @Test
//    public void testGetAdminByIdNull() {
//        // Arrange
//        when(adminDAO.findById(2)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(AdminNotFoundException.class, () -> adminService.getAdminById(2));
//    }
//
//    @Test
//    public void testGetAdminByEmailNull() {
//        // Arrange
//        when(adminDAO.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(AdminNotFoundException.class, () -> adminService.getAdminByEmail("notfound@example.com"));
//    }
//
//    @Test
//    public void testGetAllAdmins() {
//        // Arrange
//        List<Admin> adminList = Arrays.asList(admin);
//        OutgoingAdminDTO outgoingAdminDTO = new OutgoingAdminDTO();
//
//        when(adminDAO.findAll()).thenReturn(adminList);
//        when(adminMapper.toDto(admin)).thenReturn(outgoingAdminDTO);
//
//        // Act
//        List<OutgoingAdminDTO> result = adminService.getAllAdmins();
//
//        // Assert
//        assertEquals(1, result.size());
//        assertEquals(outgoingAdminDTO, result.get(0));
//    }
//
//    @Test
//    public void testUpdateAdminById() throws CustomException {
//        // Arrange
//        Map<String, String> updatedData = new HashMap<>();
//        updatedData.put("firstName", "Jane");
//        updatedData.put("lastName", "Doe");
//        updatedData.put("email", "jane.doe@example.com");
//
//        when(adminDAO.findById(1)).thenReturn(Optional.of(admin));
//        when(adminDAO.save(admin)).thenReturn(admin);
//        when(adminDAO.findByEmail("jane.doe@example.com")).thenReturn(Optional.empty());
//
//        // Act
//        Admin result = adminService.updateAdminById(1, updatedData);
//
//        // Assert
//        assertEquals("Jane", result.getFirstName());
//        assertEquals("Doe", result.getLastName());
//        assertEquals("jane.doe@example.com", result.getEmail());
//    }
//
//    @Test
//    public void testUpdateAdminByIdEmailAlreadyExists() {
//        // Arrange
//        Admin anotherAdmin = new Admin();
//        anotherAdmin.setAdminId(2);
//        anotherAdmin.setEmail("jane.doe@example.com");
//
//        Map<String, String> updatedData = new HashMap<>();
//        updatedData.put("email", "jane.doe@example.com");
//
//        when(adminDAO.findById(1)).thenReturn(Optional.of(admin));
//        when(adminDAO.findByEmail("jane.doe@example.com")).thenReturn(Optional.of(anotherAdmin));
//
//        // Act & Assert
//        assertThrows(EmailAlreadyExistException.class, () -> adminService.updateAdminById(1, updatedData));
//    }
//
//    @Test
//    public void testCreateAdmin() throws CustomException {
//        // Arrange
//        Admin newAdmin = new Admin();
//        newAdmin.setEmail("new.admin@example.com");
//        newAdmin.setPassword("password");
//
//        when(adminDAO.findByEmail("new.admin@example.com")).thenReturn(Optional.empty());
//        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
//        when(adminDAO.save(newAdmin)).thenReturn(newAdmin);
//
//        // Act
//        Admin result = adminService.createAdmin(newAdmin);
//
//        // Assert
//        assertEquals(newAdmin, result);
//        assertEquals("encodedPassword", result.getPassword());
//    }
//
//    @Test
//    public void testCreateAdminEmailAlreadyExists() {
//        // Arrange
//        when(adminDAO.findByEmail("john.doe@example.com")).thenReturn(Optional.of(admin));
//
//        Admin newAdmin = new Admin();
//        newAdmin.setEmail("john.doe@example.com");
//
//        // Act & Assert
//        assertThrows(EmailAlreadyExistException.class, () -> adminService.createAdmin(newAdmin));
//    }
//
//
//
//}
