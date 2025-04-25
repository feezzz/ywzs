-- 删除已存在的数据库并重新创建
DROP DATABASE IF EXISTS ywzs_db;
CREATE DATABASE ywzs_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE ywzs_db;

-- 创建用户表
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    status INT NOT NULL DEFAULT 1,
    dept_id BIGINT,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    last_login_time DATETIME,
    remark VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建角色表
CREATE TABLE sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    status INT NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    remark VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建用户角色关联表
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id),
    FOREIGN KEY (role_id) REFERENCES sys_role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入初始管理员用户
INSERT INTO sys_user (username, password, name, status, create_time, update_time)
VALUES ('admin', '$2a$10$mW/yJPHjyueQ1g26YxzpH.xRz7QjGqFB1NxoXqKF9ABrTB6U.7YUS', '系统管理员', 1, NOW(), NOW());

-- 插入初始角色
INSERT INTO sys_role (name, code, status, create_time, update_time)
VALUES ('管理员', 'ROLE_ADMIN', 1, NOW(), NOW());

-- 为管理员用户分配角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id 
FROM sys_user u, sys_role r 
WHERE u.username = 'admin' AND r.code = 'ROLE_ADMIN'; 