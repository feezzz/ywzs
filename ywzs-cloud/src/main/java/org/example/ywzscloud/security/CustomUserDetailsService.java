package org.example.ywzscloud.security;

import lombok.RequiredArgsConstructor;
import org.example.ywzscloud.entity.User;
import org.example.ywzscloud.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("正在加载用户: {}", username);
        
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> {
                    logger.error("用户不存在: {}", username);
                    return new UsernameNotFoundException("用户不存在: " + username);
                });

        logger.info("找到用户: {}, 状态: {}", username, user.getStatus());
        
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> {
                    logger.info("用户角色: {}", role.getCode());
                    return new SimpleGrantedAuthority(role.getCode());
                })
                .collect(Collectors.toList());

        logger.info("用户权限列表: {}", authorities);
        
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(user.getStatus() != 1)
                .build();
    }
} 