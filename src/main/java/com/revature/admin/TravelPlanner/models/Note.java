package com.revature.admin.TravelPlanner.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

// Refactored model class name from 'Notes' to 'Note' to align with naming conventions and industry standards.
@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID noteId;

    @Column(nullable = true)
    private String text;

    @JoinColumn(name = "admin_id")
    // This causing an error ('com.revature.admin.models.Note.admin' is not a collection), I have to fix it
    //@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Admin admin;

    @JoinColumn(name = "supportTicketId")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private SupportTicket supportTicket;

    private Date createdAt;

    @PrePersist
    private void onCreate(){
        createdAt = new Date();
    }

    public Note() {
    }

    public Note(UUID noteId, String text, Admin admin, SupportTicket supportTicket) {
        this.noteId = noteId;
        this.admin = admin;
        this.supportTicket = supportTicket;
        this.text =text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // Getters and setters
    public UUID getNoteId() {
        return noteId;
    }

    public void setNoteId(UUID noteId) {
        this.noteId = noteId;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public SupportTicket getSupportTicket() {
        return supportTicket;
    }

    public void setSupportTicket(SupportTicket supportTicket) {
        this.supportTicket = supportTicket;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", text='" + text + '\'' +
                ", admin=" + admin +
                ", supportTicket=" + supportTicket +
                ", createdAt=" + createdAt +
                '}';
    }
}