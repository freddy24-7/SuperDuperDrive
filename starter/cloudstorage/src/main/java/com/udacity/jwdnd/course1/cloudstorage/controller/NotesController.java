package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping()
public class NotesController {

    private final NotesService notesService;

    @PostMapping("/store-note")
    public String addNote(Authentication authentication, @ModelAttribute Notes notes, Model model) {
        try {
            //Debugging statement to illustrate data flow
            System.out.println("Received Note Data:");
            System.out.println("Note ID: " + notes.getNoteid());
            System.out.println("Title: " + notes.getNotetitle());
            System.out.println("Description: " + notes.getNotedescription());

            if (notes.getNoteid() == null) {
                notesService.insertNote(notes, authentication.getName());
            } else {
                notesService.updateNote(notes, authentication.getName());
            }
            setSuccessModelAttributes(model, authentication.getName());
        } catch (Exception e) {
            setErrorModelAttributes(model, e.getMessage());
        }
        return "result";
    }

    @GetMapping("/deletenote/{noteid}")
    public String deleteNote(Authentication authentication, Model model, @PathVariable Integer noteid) {
        try {
            notesService.deleteNote(noteid);
            setSuccessModelAttributes(model, authentication.getName());
        } catch (Exception e) {
            setErrorModelAttributes(model, e.getMessage());
        }
        return "result";
    }

    private void setErrorModelAttributes(Model model, String errorMessage) {
        model.addAttribute("result", "Error");
        model.addAttribute("errorMessage", errorMessage);
    }

    public void setSuccessModelAttributes(Model model, String userName) {
        model.addAttribute("result", "success");
        model.addAttribute("credentials", notesService.getAllNotes(userName));
    }
}
