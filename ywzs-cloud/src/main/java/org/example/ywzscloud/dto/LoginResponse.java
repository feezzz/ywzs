package org.example.ywzscloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private boolean success = true;  // 默认为 true
    private ResponseData data;

    public LoginResponse(ResponseData data) {
        this.success = true;
        this.data = data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseData {
        private String token;  // 为了兼容前端，保留token字段
        private UserInfo user;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {
        private Long id;
        private String username;
        private String fullName;
        private String email;
        private String phone;
    }
} 