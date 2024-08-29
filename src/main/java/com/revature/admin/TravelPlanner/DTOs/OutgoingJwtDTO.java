package com.revature.admin.TravelPlanner.DTOs;

import java.util.UUID;

public class OutgoingJwtDTO {
    private UUID adminId;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isMaster;
    private String token;

    public OutgoingJwtDTO(UUID adminId, String firstName, String lastName, String email, boolean isMaster, String token) {
        this.adminId = adminId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isMaster = isMaster;
        this.token = token;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public String getToken() {
        return token;
    }
}
