package com.example.realtimedashboard.controller;

import com.example.realtimedashboard.model.Task;
import com.example.realtimedashboard.model.User;
import com.example.realtimedashboard.repository.TaskRepository;
import com.example.realtimedashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Task> getAllTasks(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return taskRepository.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        task.setUser(user);
        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task taskRequest, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Task task = taskRepository.findById(id).orElseThrow();
        if (!task.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        task.setStatus(taskRequest.getStatus());
        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Task task = taskRepository.findById(id).orElseThrow();
        if (!task.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        taskRepository.delete(task);
        return ResponseEntity.ok("Deleted");
    }
} 