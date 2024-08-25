package com.revature.admin.TravelPlanner.DTOs;
import com.revature.admin.TravelPlanner.enums.TicketStatus;
import com.revature.admin.TravelPlanner.enums.TicketType;

import java.util.Date;

public class OutgoingNoteDTO {

    private int noteId;
    private int adminId;
    private int ticketId;
    private String text;
    private Date createdAt;

    public OutgoingNoteDTO(int noteId, int adminId, int ticketId, String text, Date createdAt) {
        this.noteId = noteId;
        this.adminId = adminId;
        this.ticketId = ticketId;
        this.text = text;
        this.createdAt = createdAt;
    }



    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
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
