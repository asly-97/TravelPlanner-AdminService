package com.revature.admin.TravelPlanner.DTOs;
import com.revature.admin.TravelPlanner.enums.TicketStatus;
import com.revature.admin.TravelPlanner.enums.TicketType;

public class OutgoingNoteDTO {

    //Model Variable
    private int noteId;
    private int adminId;
    private int ticketId;
    private int userId;
    private TicketStatus status;
    private TicketType type;
    private String description;
    private long dateCreated;
    private long dateResolved;

    //Constructors
    public OutgoingNoteDTO() {
    }

    public OutgoingNoteDTO(int noteId, int adminId, int ticketId, int userId, TicketStatus status, TicketType type,
                           String description, long dateCreated, long dateResolved) {
        this.noteId = noteId;
        this.adminId = adminId;
        this.ticketId = ticketId;
        this.userId = userId;
        this.status = status;
        this.type = type;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateResolved = dateResolved;
    }

    //Getter and Setter

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDateResolved() {
        return dateResolved;
    }

    public void setDateResolved(long dateResolved) {
        this.dateResolved = dateResolved;
    }

    @Override
    public String toString() {
        return "OutgoingNoteDTO{" +
                "noteId=" + noteId +
                ", adminId=" + adminId +
                ", ticketId=" + ticketId +
                ", userId=" + userId +
                ", status=" + status +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateResolved=" + dateResolved +
                '}';
    }
}
