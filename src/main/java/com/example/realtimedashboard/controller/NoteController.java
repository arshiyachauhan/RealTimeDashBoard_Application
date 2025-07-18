package com.example.realtimedashboard.controller;

import com.example.realtimedashboard.model.Note;
import com.example.realtimedashboard.model.User;
import com.example.realtimedashboard.repository.NoteRepository;
import com.example.realtimedashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Note> getAllNotes(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return noteRepository.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<?> createNote(@RequestBody Note note, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        note.setUser(user);
        if (note.getCreatedAt() == null) {
            note.setCreatedAt(LocalDateTime.now());
        }
        noteRepository.save(note);
        return ResponseEntity.ok(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(@PathVariable Long id, @RequestBody Note noteRequest, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Note note = noteRepository.findById(id).orElseThrow();
        if (!note.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        note.setTitle(noteRequest.getTitle());
        note.setContent(noteRequest.getContent());
        // Do not overwrite createdAt
        noteRepository.save(note);
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Note note = noteRepository.findById(id).orElseThrow();
        if (!note.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        noteRepository.delete(note);
        return ResponseEntity.ok("Deleted");
    }
} 