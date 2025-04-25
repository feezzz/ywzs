package org.example.ywzscloud.repository;

import org.example.ywzscloud.entity.SystemLog;
import org.example.ywzscloud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    
    List<SystemLog> findByUser(User user);
    
    List<SystemLog> findByLevel(SystemLog.LogLevel level);
    
    List<SystemLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    List<SystemLog> findByActionContaining(String action);
} 