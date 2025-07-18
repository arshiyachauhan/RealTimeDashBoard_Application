package com.example.realtimedashboard.repository;

import com.example.realtimedashboard.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findByUserId(Long userId);
} 