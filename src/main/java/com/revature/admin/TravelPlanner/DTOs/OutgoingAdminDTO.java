package com.revature.admin.TravelPlanner.DTOs;

import java.util.Date;
import java.util.List;

public class OutgoingAdminDTO {

    //Model variables
    private int adminId;
    private boolean isMaster = false;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> notes;
    private Date createdAt;


    //Constructors
    public OutgoingAdminDTO() {
    }

    public OutgoingAdminDTO(int adminId, boolean isMaster, String firstName, String lastName, String email, List<String> notes, Date createdAt) {
        this.adminId = adminId;
        this.isMaster = isMaster;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    //Getter and Setter
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OutgoingAdminDTO{" +
                "adminId=" + adminId +
                ", isMaster=" + isMaster +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", notes=" + notes +
                ", createdAt=" + createdAt +
                '}';
    }
}
