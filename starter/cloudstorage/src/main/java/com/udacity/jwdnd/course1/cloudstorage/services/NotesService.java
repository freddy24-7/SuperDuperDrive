package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotesService {

    private final NotesMapper notesMapper;
    private final UserMapper userMapper;
    private List<Notes> notes;

    public void insertNote(Notes notes, String userName) {
        Notes newNotes = createNoteWithUserId(notes, userName);
        notesMapper.insertNote(newNotes);
    }

    public void updateNote(Notes notes, String userName) {
        Notes existingNotes = notesMapper.getNoteById(notes.getNoteid());
        if (existingNotes != null) {
            existingNotes.setNotetitle(notes.getNotetitle());
            existingNotes.setNotedescription(notes.getNotedescription());
            notesMapper.updateNote(existingNotes);
        }
    }

    public void deleteNote(Integer noteId) {
        notesMapper.deleteNote(noteId);
    }

    public List<Notes> getAllNotes(String userName) {
        notes = notesMapper.getAllNotes(userMapper.getUser(userName).getUserId());
        return notesMapper.getAllNotes(userMapper.getUser(userName).getUserId());
    }

    private Notes createNoteWithUserId(Notes notes, String userName) {
        return new Notes(notes.getNoteid(), notes.getNotetitle(), notes.getNotedescription(), userMapper.getUser(userName).getUserId());
    }
}
