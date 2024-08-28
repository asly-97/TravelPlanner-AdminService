package com.revature.admin.TravelPlanner.controllers;

import com.revature.admin.TravelPlanner.exceptions.CustomException;
import com.revature.admin.TravelPlanner.models.Note;
import com.revature.admin.TravelPlanner.services.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/note")
@CrossOrigin
public class NoteController {
    Logger log = LoggerFactory.getLogger(NoteController.class);
    private NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PatchMapping("/{noteId}")
    public ResponseEntity<Note> updateNoteText(
            @PathVariable int noteId,
            @RequestBody String updatedText) throws CustomException {
        log.debug("Endpoint PATCH ./note/{} reached",noteId);

        Note updatedNote = noteService.updateNoteText(noteId,updatedText);

        return ResponseEntity.accepted().body(updatedNote);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<String> deleteNote(@PathVariable int noteId) throws CustomException{
        log.debug("Endpoint DELETE ./note/{} reached",noteId);
        String message = noteService.deleteNote(noteId);
        return ResponseEntity.accepted().body(message);
    }

    // handles all the custom exceptions
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException( CustomException e){
        log.warn("Exception was thrown: {}", e.getMessageText());
        return ResponseEntity.status(e.getStatus()).body(e.getMessageText());
    }


}