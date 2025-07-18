package com.example.realtimedashboard.controller;

import com.example.realtimedashboard.model.Reminder;
import com.example.realtimedashboard.model.Task;
import com.example.realtimedashboard.model.User;
import com.example.realtimedashboard.repository.ReminderRepository;
import com.example.realtimedashboard.repository.TaskRepository;
import com.example.realtimedashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {
    @Autowired
    private ReminderRepository reminderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Reminder> getAllReminders(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return reminderRepository.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<?> createReminder(@RequestBody Reminder reminder, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        reminder.setUser(user);
        if (reminder.getRemindAt() == null) {
            reminder.setRemindAt(LocalDateTime.now());
        }
        if (reminder.getTask() != null && reminder.getTask().getId() != null) {
            Task task = taskRepository.findById(reminder.getTask().getId()).orElse(null);
            reminder.setTask(task);
        }
        reminderRepository.save(reminder);
        return ResponseEntity.ok(reminder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReminder(@PathVariable Long id, @RequestBody Reminder reminderRequest, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Reminder reminder = reminderRepository.findById(id).orElseThrow();
        if (!reminder.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        reminder.setMessage(reminderRequest.getMessage());
        // Do not overwrite remindAt
        if (reminderRequest.getTask() != null && reminderRequest.getTask().getId() != null) {
            Task task = taskRepository.findById(reminderRequest.getTask().getId()).orElse(null);
            reminder.setTask(task);
        } else {
            reminder.setTask(null);
        }
        reminderRepository.save(reminder);
        return ResponseEntity.ok(reminder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReminder(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Reminder reminder = reminderRepository.findById(id).orElseThrow();
        if (!reminder.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        reminderRepository.delete(reminder);
        return ResponseEntity.ok("Deleted");
    }
} 