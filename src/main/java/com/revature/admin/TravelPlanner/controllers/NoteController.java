package com.revature.admin.TravelPlanner.controllers;

import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.models.Note;
import com.revature.admin.TravelPlanner.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/note")
@CrossOrigin
public class NoteController {

    private NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PatchMapping("/{noteId}")
    public ResponseEntity<Note> updateNoteText(
            @PathVariable int noteId,
            @RequestBody String updatedText) throws CustomException {

        Note updatedNote = noteService.updateNoteText(noteId,updatedText);

        return ResponseEntity.accepted().body(updatedNote);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<String> deleteNote(@PathVariable int noteId) throws CustomException{
        String message = noteService.deleteNote(noteId);
        return ResponseEntity.accepted().body(message);
    }

    // handles all the custom exceptions
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException( CustomException e){
        return ResponseEntity.status(e.getStatus()).body(e.getMessageText());
    }


}