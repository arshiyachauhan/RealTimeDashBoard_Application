package com.example.realtimedashboard.controller;

import com.example.realtimedashboard.model.Metric;
import com.example.realtimedashboard.model.User;
import com.example.realtimedashboard.repository.MetricRepository;
import com.example.realtimedashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class MetricController {
    @Autowired
    private MetricRepository metricRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Metric> getAllMetrics(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return metricRepository.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<?> createMetric(@RequestBody Metric metric, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        metric.setUser(user);
        if (metric.getRecordedAt() == null) {
            metric.setRecordedAt(LocalDateTime.now());
        }
        metricRepository.save(metric);
        return ResponseEntity.ok(metric);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMetric(@PathVariable Long id, @RequestBody Metric metricRequest, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Metric metric = metricRepository.findById(id).orElseThrow();
        if (!metric.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        metric.setName(metricRequest.getName());
        metric.setValue(metricRequest.getValue());
        metric.setUnit(metricRequest.getUnit());
        metric.setRecordedAt(metricRequest.getRecordedAt());
        metricRepository.save(metric);
        return ResponseEntity.ok(metric);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMetric(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Metric metric = metricRepository.findById(id).orElseThrow();
        if (!metric.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        metricRepository.delete(metric);
        return ResponseEntity.ok("Deleted");
    }
} 