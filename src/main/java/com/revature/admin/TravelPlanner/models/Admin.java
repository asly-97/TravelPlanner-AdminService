package com.revature.admin.TravelPlanner.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "admins")
public class Admin {

    //Getter and Setters
    //Model Variables
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID adminId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // For master admin, this field will be true; otherwise,
    // it will default to false
    @Column(nullable = false)
    private boolean master;

    @Column(nullable = false)
    private Date createdAt;

    @PrePersist
    private void onCreate() {
        // Set master to false by default upon creation
        master = false;
        createdAt = new Date();
    }



    //Relational Variable(s)
    @JsonIgnore
    @OneToMany(mappedBy = "admin", fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private List<Note> notes;

    //Constructors
    public Admin() {
    }

    public Admin(UUID adminId, String firstName, String lastName, String email, String password) {
        this.adminId = adminId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    //Getter and Setter
    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", master=" + master +
                ", createdAt=" + createdAt +
                ", notes=" + notes +
                '}';
    }
}
