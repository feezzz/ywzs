package org.example.ywzscloud.controller;

import org.example.ywzscloud.dto.LoginRequest;
import org.example.ywzscloud.dto.LoginResponse;
import org.example.ywzscloud.entity.User;
import org.example.ywzscloud.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", maxAge = 3600)
@RequiredArgsConstructor
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/test-password")
    public ResponseEntity<?> testPassword() {
        String rawPassword = "admin123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        logger.info("原始密码: {}", rawPassword);
        logger.info("加密后的密码: {}", encodedPassword);
        logger.info("验证结果: {}", passwordEncoder.matches(rawPassword, encodedPassword));
        
        Map<String, String> result = new HashMap<>();
        result.put("rawPassword", rawPassword);
        result.put("encodedPassword", encodedPassword);
        result.put("matches", String.valueOf(passwordEncoder.matches(rawPassword, encodedPassword)));
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            logger.info("开始处理登录请求，用户名: {}", loginRequest.getUsername());
            
            // 获取用户信息并验证密码
            Optional<User> userOptional = userService.findUserByUsername(loginRequest.getUsername());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                logger.debug("数据库中的密码: {}", user.getPassword());
                logger.debug("密码匹配结果: {}", passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()));
            }
            
            // 使用AuthenticationManager进行认证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            
            // 认证成功，设置SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 获取用户信息
            userOptional = userService.findUserByUsername(loginRequest.getUsername());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                
                // 更新最后登录时间
                user.setLastLoginTime(LocalDateTime.now());
                userService.updateUser(user);
                
                // 返回用户信息
                LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhone()
                );
                
                logger.info("登录成功，返回用户信息");
                return ResponseEntity.ok(new LoginResponse(new LoginResponse.ResponseData(null, userInfo)));
            } else {
                logger.error("用户不存在: {}", loginRequest.getUsername());
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            logger.error("登录失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "登录失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("用户名已被使用");
        }
        
        if (user.getEmail() != null && userService.findUserByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("邮箱已被使用");
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        logger.info("用户退出登录");
        return ResponseEntity.ok().body("退出登录成功");
    }
    
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<User> userOptional = userService.findUserByUsername(authentication.getName());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhone()
                );
                return ResponseEntity.ok(userInfo);
            }
        }
        return ResponseEntity.status(401).body("未登录");
    }

    @PostMapping("/test-password")
    public ResponseEntity<?> testPassword(@RequestBody Map<String, String> request) {
        String rawPassword = request.get("password");
        String storedHash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi";
        
        // 测试密码匹配
        boolean matches = passwordEncoder.matches(rawPassword, storedHash);
        
        // 生成新的哈希值（用于比较）
        String newHash = passwordEncoder.encode(rawPassword);
        
        Map<String, Object> response = new HashMap<>();
        response.put("rawPassword", rawPassword);
        response.put("storedHash", storedHash);
        response.put("newHash", newHash);
        response.put("matches", matches);
        
        return ResponseEntity.ok(response);
    }
} 