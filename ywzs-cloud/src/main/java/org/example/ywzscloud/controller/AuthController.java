package org.example.ywzscloud.controller;

import org.example.ywzscloud.dto.LoginRequest;
import org.example.ywzscloud.dto.LoginResponse;
import org.example.ywzscloud.entity.User;
import org.example.ywzscloud.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", maxAge = 3600)
@RequiredArgsConstructor
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    
    @GetMapping("/test-password")
    public ResponseEntity<?> testPassword() {
        String rawPassword = "admin123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        logger.info("原始密码: {}", rawPassword);
        logger.info("加密后的密码: {}", encodedPassword);
        logger.info("验证结果: {}", passwordEncoder.matches(rawPassword, encodedPassword));
        
        return ResponseEntity.ok(new HashMap<String, String>() {{
            put("rawPassword", rawPassword);
            put("encodedPassword", encodedPassword);
            put("matches", String.valueOf(passwordEncoder.matches(rawPassword, encodedPassword)));
        }});
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            logger.info("开始处理登录请求，用户名: {}", loginRequest.getUsername());
            
            Optional<User> userOptional = userService.findUserByUsername(loginRequest.getUsername());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                
                // 验证密码
                if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
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
                    
                    // 为了兼容前端，返回一个固定的token
                    String token = "dummy-token-" + System.currentTimeMillis();
                    
                    logger.info("登录成功，返回用户信息");
                    return ResponseEntity.ok(new LoginResponse(new LoginResponse.ResponseData(token, userInfo)));
                } else {
                    logger.error("密码错误");
                    return ResponseEntity.badRequest().body("密码错误");
                }
            } else {
                logger.error("用户不存在: {}", loginRequest.getUsername());
                return ResponseEntity.badRequest().body("用户不存在");
            }
        } catch (Exception e) {
            logger.error("登录失败", e);
            return ResponseEntity.badRequest().body("登录失败: " + e.getMessage());
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
        logger.info("用户退出登录");
        return ResponseEntity.ok().body("退出登录成功");
    }
} 