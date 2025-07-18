package com.example.realtimedashboard.controller;

import com.example.realtimedashboard.model.Note;
import com.example.realtimedashboard.model.Task;
import com.example.realtimedashboard.model.Metric;
import com.example.realtimedashboard.model.Transaction;
import com.example.realtimedashboard.model.User;
import com.example.realtimedashboard.repository.NoteRepository;
import com.example.realtimedashboard.repository.TaskRepository;
import com.example.realtimedashboard.repository.MetricRepository;
import com.example.realtimedashboard.repository.TransactionRepository;
import com.example.realtimedashboard.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private MetricRepository metricRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/overview")
    public ResponseEntity<?> getOverview(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        List<Note> notes = noteRepository.findByUserId(user.getId());
        List<Task> tasks = taskRepository.findByUserId(user.getId());
        List<Metric> metrics = metricRepository.findByUserId(user.getId());
        List<Transaction> transactions = transactionRepository.findByUserId(user.getId());

        double income = transactions.stream().filter(t -> "INCOME".equalsIgnoreCase(t.getType())).mapToDouble(Transaction::getAmount).sum();
        double expense = transactions.stream().filter(t -> "EXPENSE".equalsIgnoreCase(t.getType())).mapToDouble(Transaction::getAmount).sum();
        double balance = income - expense;

        DashboardOverview overview = new DashboardOverview(notes, tasks, metrics, transactions, income, expense, balance);
        return ResponseEntity.ok(overview);
    }

    @Data
    static class DashboardOverview {
        private final List<Note> notes;
        private final List<Task> tasks;
        private final List<Metric> metrics;
        private final List<Transaction> transactions;
        private final double income;
        private final double expense;
        private final double balance;
    }
} 