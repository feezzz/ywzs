package org.example.ywzscloud.service.impl;

import org.example.ywzscloud.entity.Task;
import org.example.ywzscloud.entity.User;
import org.example.ywzscloud.repository.TaskRepository;
import org.example.ywzscloud.repository.UserRepository;
import org.example.ywzscloud.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }
    
    @Override
    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }
    
    @Override
    @Transactional
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }
    
    @Override
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public Task updateTask(Task task) {
        if (!taskRepository.existsById(task.getId())) {
            throw new RuntimeException("Task not found with id: " + task.getId());
        }
        return taskRepository.save(task);
    }
    
    @Override
    public List<Task> findTasksByAssignedUser(User user) {
        return taskRepository.findByAssignedTo(user);
    }
    
    @Override
    public List<Task> findTasksByCreatedUser(User user) {
        return taskRepository.findByCreatedBy(user);
    }
    
    @Override
    public List<Task> findTasksByStatus(Task.TaskStatus status) {
        return taskRepository.findByStatus(status);
    }
    
    @Override
    public List<Task> findOverdueTasks(LocalDateTime currentDate) {
        return taskRepository.findByDueDateBefore(currentDate);
    }
    
    @Override
    @Transactional
    public Task markTaskAsCompleted(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        
        task.setStatus(Task.TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());
        
        return taskRepository.save(task);
    }
    
    @Override
    @Transactional
    public Task assignTaskToUser(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        task.setAssignedTo(user);
        
        return taskRepository.save(task);
    }
} 