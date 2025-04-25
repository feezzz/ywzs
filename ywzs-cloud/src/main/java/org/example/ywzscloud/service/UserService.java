package org.example.ywzscloud.service;

import org.example.ywzscloud.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    List<User> findAllUsers();
    
    Page<User> findUsers(String username, String fullName, Integer status, Pageable pageable);
    
    Optional<User> findUserById(Long id);
    
    Optional<User> findUserByUsername(String username);
    
    Optional<User> findUserByEmail(String email);
    
    User saveUser(User user);
    
    User updateUser(User user);
    
    void deleteUser(Long id);
    
    boolean existsByUsername(String username);
} 