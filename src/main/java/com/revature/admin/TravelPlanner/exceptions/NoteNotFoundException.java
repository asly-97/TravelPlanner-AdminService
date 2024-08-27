package com.revature.admin.TravelPlanner.exceptions;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class NoteNotFoundException extends CustomException{
    public NoteNotFoundException(UUID id){
        super("Note with Id: " + id + " was not found.");
    }

    @Override
    public int getStatus() {
        return HttpStatus.NOT_FOUND.value();
    }
}
