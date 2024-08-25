package com.revature.admin.TravelPlanner.exceptions;

public class InvalidNoteTextException extends CustomException{
    public InvalidNoteTextException() {
        super("Note's description is required.");
    }
}
