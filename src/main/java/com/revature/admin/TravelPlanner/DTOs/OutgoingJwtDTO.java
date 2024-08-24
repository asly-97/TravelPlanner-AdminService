package com.revature.admin.TravelPlanner.DTOs;

public class OutgoingJwtDTO {
    private int adminId;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isMaster;
    private String token;

    public OutgoingJwtDTO(int adminId, String firstName, String lastName, String email, boolean isMaster, String token) {
        this.adminId = adminId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isMaster = isMaster;
        this.token = token;
    }

    public int getAdminId() {
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
