package org.example.ywzscloud.service;

import org.example.ywzscloud.entity.Task;
import org.example.ywzscloud.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    
    List<Task> findAllTasks();
    
    Optional<Task> findTaskById(Long id);
    
    Task saveTask(Task task);
    
    void deleteTask(Long id);
    
    Task updateTask(Task task);
    
    List<Task> findTasksByAssignedUser(User user);
    
    List<Task> findTasksByCreatedUser(User user);
    
    List<Task> findTasksByStatus(Task.TaskStatus status);
    
    List<Task> findOverdueTasks(LocalDateTime currentDate);
    
    Task markTaskAsCompleted(Long taskId);
    
    Task assignTaskToUser(Long taskId, Long userId);
} 