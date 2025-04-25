package org.example.ywzscloud.controller;

import org.example.ywzscloud.dto.UserDTO;
import org.example.ywzscloud.entity.User;
import org.example.ywzscloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) Integer status
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
            Page<User> users = userService.findUsers(username, fullName, status, pageRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("users", users.getContent());
            response.put("currentPage", users.getNumber());
            response.put("totalItems", users.getTotalElements());
            response.put("totalPages", users.getTotalPages());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("获取用户列表失败", e);
            return ResponseEntity.badRequest().body("获取用户列表失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            if (userService.existsByUsername(userDTO.getUsername())) {
                return ResponseEntity.badRequest().body("用户名已存在");
            }
            
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setFullName(userDTO.getFullName());
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setStatus(userDTO.getStatus());
            user.setDeptId(userDTO.getDeptId());
            user.setRemark(userDTO.getRemark());
            
            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            logger.error("创建用户失败", e);
            return ResponseEntity.badRequest().body("创建用户失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        try {
            return userService.findUserById(id)
                    .map(user -> {
                        user.setFullName(userDTO.getFullName());
                        user.setEmail(userDTO.getEmail());
                        user.setPhone(userDTO.getPhone());
                        user.setStatus(userDTO.getStatus());
                        user.setDeptId(userDTO.getDeptId());
                        user.setRemark(userDTO.getRemark());
                        
                        // 如果提供了新密码，则更新密码
                        if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
                            user.setPassword(userDTO.getPassword());
                        }
                        
                        User updatedUser = userService.updateUser(user);
                        return ResponseEntity.ok(updatedUser);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("更新用户失败", e);
            return ResponseEntity.badRequest().body("更新用户失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            if (!userService.findUserById(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("删除用户失败", e);
            return ResponseEntity.badRequest().body("删除用户失败: " + e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> status) {
        try {
            return userService.findUserById(id)
                    .map(user -> {
                        user.setStatus(status.get("status"));
                        User updatedUser = userService.updateUser(user);
                        return ResponseEntity.ok(updatedUser);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("更新用户状态失败", e);
            return ResponseEntity.badRequest().body("更新用户状态失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> passwords) {
        try {
            return userService.findUserById(id)
                    .map(user -> {
                        user.setPassword(passwords.get("password"));
                        User updatedUser = userService.updateUser(user);
                        return ResponseEntity.ok().build();
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("重置密码失败", e);
            return ResponseEntity.badRequest().body("重置密码失败: " + e.getMessage());
        }
    }
} 