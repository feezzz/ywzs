package org.example.ywzscloud.config;

import lombok.RequiredArgsConstructor;
import org.example.ywzscloud.entity.User;
import org.example.ywzscloud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private final UserService userService;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (!userService.existsByUsername("admin")) {
                logger.info("创建默认管理员用户");
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword("admin123"); // 将在service层加密
                adminUser.setFullName("系统管理员");
                adminUser.setEmail("admin@example.com");
                adminUser.setPhone("13800000000");
                adminUser.setStatus(1);
                adminUser.setRemark("系统自动创建的管理员账号");
                userService.saveUser(adminUser);
                logger.info("默认管理员用户创建成功");
            }
        };
    }
} 