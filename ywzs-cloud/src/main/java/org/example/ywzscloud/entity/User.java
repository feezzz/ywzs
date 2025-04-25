package org.example.ywzscloud.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = "roles")
@Table(name = "sys_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "name", nullable = false)
    private String fullName;
    
    @Column
    private String email;
    
    @Column
    private String phone;
    
    @Column(nullable = false)
    private Integer status = 1;  // 0-禁用, 1-启用
    
    @Column(name = "dept_id")
    private Long deptId;
    
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "update_time")
    private LocalDateTime updatedAt;
    
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
    
    @Column
    private String remark;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "sys_user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = 1;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 