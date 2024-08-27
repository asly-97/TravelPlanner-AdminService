package com.revature.admin.TravelPlanner.DTOs;

import java.util.Date;
import java.util.UUID;

public class OutgoingNoteDTO {

    private UUID noteId;
    private UUID adminId;
    private UUID ticketId;
    private String text;
    private Date createdAt;

    public OutgoingNoteDTO(UUID noteId, UUID adminId, UUID ticketId, String text, Date createdAt) {
        this.noteId = noteId;
        this.adminId = adminId;
        this.ticketId = ticketId;
        this.text = text;
        this.createdAt = createdAt;
    }



    public UUID getNoteId() {
        return noteId;
    }

    public void setNoteId(UUID noteId) {
        this.noteId = noteId;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public void setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OutgoingNoteDTO{" +
                "noteId=" + noteId +
                ", adminId=" + adminId +
                ", ticketId=" + ticketId +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
