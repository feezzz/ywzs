package org.example.ywzscloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/password")
    public Map<String, String> testPassword() {
        String rawPassword = "admin123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        logger.info("原始密码: {}", rawPassword);
        logger.info("加密后的密码: {}", encodedPassword);
        logger.info("验证结果: {}", passwordEncoder.matches(rawPassword, encodedPassword));
        logger.info("验证数据库密码: {}", passwordEncoder.matches(rawPassword, "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi"));
        
        Map<String, String> result = new HashMap<>();
        result.put("rawPassword", rawPassword);
        result.put("encodedPassword", encodedPassword);
        result.put("matches", String.valueOf(passwordEncoder.matches(rawPassword, encodedPassword)));
        result.put("matchesDatabase", String.valueOf(passwordEncoder.matches(rawPassword, "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi")));
        
        return result;
    }
} 