package org.example.ywzscloud.dto;

import lombok.Data;
import org.example.ywzscloud.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserDTO {
    
    private Long id;
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 50, message = "用户名长度必须在4-50个字符之间")
    private String username;
    
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    private String password;
    
    @NotBlank(message = "姓名不能为空")
    @Size(max = 100, message = "姓名长度不能超过100个字符")
    private String fullName;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Size(max = 20, message = "电话长度不能超过20个字符")
    private String phone;
    
    private Integer status = 1;
    
    private Long deptId;
    
    private String remark;

    // 从User实体转换为DTO
    public static UserDTO fromUser(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setStatus(user.getStatus());
        return dto;
    }

    // 从DTO转换为User实体
    public User toUser() {
        User user = new User();
        user.setId(this.getId());
        user.setUsername(this.getUsername());
        user.setPassword(this.getPassword());
        user.setFullName(this.getFullName());
        user.setEmail(this.getEmail());
        user.setPhone(this.getPhone());
        user.setStatus(this.getStatus());
        return user;
    }
} 