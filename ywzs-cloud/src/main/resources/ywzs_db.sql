-- 删除已存在的数据库并重新创建
DROP DATABASE IF EXISTS ywzs_db;
CREATE DATABASE ywzs_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE ywzs_db;

-- 创建部门表
CREATE TABLE sys_dept (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT COMMENT '父部门ID',
    name VARCHAR(50) NOT NULL COMMENT '部门名称',
    sort INT DEFAULT 0 COMMENT '显示顺序',
    leader VARCHAR(20) COMMENT '负责人',
    phone VARCHAR(11) COMMENT '联系电话',
    email VARCHAR(50) COMMENT '邮箱',
    status INT DEFAULT 1 COMMENT '部门状态（1正常 0停用）',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 创建用户表
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 1-启用 0-禁用',
    dept_id BIGINT COMMENT '部门ID',
    avatar VARCHAR(255) COMMENT '头像地址',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    last_login_time DATETIME COMMENT '最后登录时间',
    remark VARCHAR(500) COMMENT '备注',
    FOREIGN KEY (dept_id) REFERENCES sys_dept(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建角色表
CREATE TABLE sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '角色名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    status INT NOT NULL DEFAULT 1 COMMENT '状态 1-启用 0-禁用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 创建菜单表
CREATE TABLE sys_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT COMMENT '父菜单ID',
    name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    path VARCHAR(200) COMMENT '路由路径',
    component VARCHAR(255) COMMENT '组件路径',
    perms VARCHAR(100) COMMENT '权限标识',
    type INT COMMENT '类型 0-目录 1-菜单 2-按钮',
    icon VARCHAR(100) COMMENT '图标',
    sort INT DEFAULT 0 COMMENT '排序',
    status INT DEFAULT 1 COMMENT '状态 1-启用 0-禁用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 创建用户角色关联表
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id),
    FOREIGN KEY (role_id) REFERENCES sys_role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 创建角色菜单关联表
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id),
    FOREIGN KEY (role_id) REFERENCES sys_role(id),
    FOREIGN KEY (menu_id) REFERENCES sys_menu(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 创建操作日志表
CREATE TABLE sys_operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT COMMENT '操作用户ID',
    operation VARCHAR(50) COMMENT '操作类型',
    method VARCHAR(100) COMMENT '请求方法',
    params TEXT COMMENT '请求参数',
    time BIGINT COMMENT '执行时长(毫秒)',
    ip VARCHAR(50) COMMENT 'IP地址',
    create_time DATETIME COMMENT '创建时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 插入初始部门数据
INSERT INTO sys_dept (id, parent_id, name, sort, status, create_time, update_time)
VALUES (1, 0, '总公司', 0, 1, NOW(), NOW());

-- 插入初始管理员用户
INSERT INTO sys_user (username, password, name, status, dept_id, create_time, update_time)
VALUES ('admin', '$2a$10$mW/yJPHjyueQ1g26YxzpH.xRz7QjGqFB1NxoXqKF9ABrTB6U.7YUS', '系统管理员', 1, 1, NOW(), NOW());

-- 插入初始角色
INSERT INTO sys_role (name, code, status, create_time, update_time)
VALUES ('超级管理员', 'ROLE_ADMIN', 1, NOW(), NOW());

-- 插入基础菜单数据
INSERT INTO sys_menu (id, parent_id, name, path, component, perms, type, icon, sort, status, create_time, update_time) VALUES
(1, 0, '系统管理', '/system', 'Layout', NULL, 0, 'system', 1, 1, NOW(), NOW()),
(2, 1, '用户管理', 'user', 'system/user/index', 'system:user:list', 1, 'user', 1, 1, NOW(), NOW()),
(3, 1, '角色管理', 'role', 'system/role/index', 'system:role:list', 1, 'role', 2, 1, NOW(), NOW()),
(4, 1, '菜单管理', 'menu', 'system/menu/index', 'system:menu:list', 1, 'menu', 3, 1, NOW(), NOW()),
(5, 1, '部门管理', 'dept', 'system/dept/index', 'system:dept:list', 1, 'dept', 4, 1, NOW(), NOW()),
(6, 2, '用户查询', NULL, NULL, 'system:user:query', 2, NULL, 1, 1, NOW(), NOW()),
(7, 2, '用户新增', NULL, NULL, 'system:user:add', 2, NULL, 2, 1, NOW(), NOW()),
(8, 2, '用户修改', NULL, NULL, 'system:user:edit', 2, NULL, 3, 1, NOW(), NOW()),
(9, 2, '用户删除', NULL, NULL, 'system:user:remove', 2, NULL, 4, 1, NOW(), NOW());

-- 为管理员用户分配角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id 
FROM sys_user u, sys_role r 
WHERE u.username = 'admin' AND r.code = 'ROLE_ADMIN';

-- 为超级管理员角色分配所有菜单权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT r.id, m.id
FROM sys_role r, sys_menu m
WHERE r.code = 'ROLE_ADMIN'; 