package org.example.ywzscloud.repository;

import org.example.ywzscloud.entity.Task;
import org.example.ywzscloud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByAssignedTo(User user);
    
    List<Task> findByCreatedBy(User user);
    
    List<Task> findByStatus(Task.TaskStatus status);
    
    List<Task> findByDueDateBefore(LocalDateTime date);
    
    List<Task> findByAssignedToAndStatus(User user, Task.TaskStatus status);
} 