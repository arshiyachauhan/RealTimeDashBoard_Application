package com.example.realtimedashboard.controller;

import com.example.realtimedashboard.model.Transaction;
import com.example.realtimedashboard.model.User;
import com.example.realtimedashboard.repository.TransactionRepository;
import com.example.realtimedashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Transaction> getAllTransactions(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return transactionRepository.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        transaction.setUser(user);
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDate.now());
        }
        transactionRepository.save(transaction);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable Long id, @RequestBody Transaction transactionRequest, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Transaction transaction = transactionRepository.findById(id).orElseThrow();
        if (!transaction.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        transaction.setType(transactionRequest.getType());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        // Do not overwrite date
        transactionRepository.save(transaction);
        return ResponseEntity.ok(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Transaction transaction = transactionRepository.findById(id).orElseThrow();
        if (!transaction.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        transactionRepository.delete(transaction);
        return ResponseEntity.ok("Deleted");
    }
} 