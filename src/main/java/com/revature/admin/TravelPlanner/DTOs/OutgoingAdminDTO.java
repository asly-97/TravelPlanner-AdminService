package com.revature.admin.TravelPlanner.DTOs;

import java.util.List;

public class OutgoingAdminDTO {

    //Model variables
    private String lastName;
    private String email;
    private List<String> notes;

    //Constructors
    public OutgoingAdminDTO() {
    }

    public OutgoingAdminDTO(String lastName, String email, List<String> notes) {
        this.lastName = lastName;
        this.email = email;
        this.notes = notes;
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

    @Override
    public String toString() {
        return "OutgoingAdminDTO{" +
                "lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", notes=" + notes +
                '}';
    }
}
