package com.revature.admin.TravelPlanner.DTOs;


import com.revature.admin.TravelPlanner.enums.TicketStatus;
import com.revature.admin.TravelPlanner.enums.TicketType;
import com.revature.admin.TravelPlanner.models.Note;

public class OutgoingSupportTicketDTO {

    //Model Variables
    private int supportTicketId;
    private int userId;
    private String description;
    private TicketStatus status;
    private TicketType type;
    private long createdDate;
    private long resolvedDate;
    private OutgoingNoteDTO note;

    public OutgoingSupportTicketDTO(int supportTicketId,
                                    int userId,
                                    String description,
                                    TicketStatus status,
                                    TicketType type,
                                    long createdDate,
                                    long resolvedDate,
                                    OutgoingNoteDTO note) {
        this.supportTicketId = supportTicketId;
        this.userId = userId;
        this.description = description;
        this.status = status;
        this.type = type;
        this.createdDate = createdDate;
        this.resolvedDate = resolvedDate;
        this.note = note;
    }

    public int getSupportTicketId() {
        return supportTicketId;
    }

    public void setSupportTicketId(int supportTicketId) {
        this.supportTicketId = supportTicketId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(long resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public OutgoingNoteDTO getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "OutgoingSupportTicketDTO{" +
                "supportTicketId=" + supportTicketId +
                ", userId=" + userId +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", createdDate=" + createdDate +
                ", resolvedDate=" + resolvedDate +
                ", note=" + note +
                '}';
    }
}
