-- 插入角色
INSERT INTO sys_role (name, code, description)
SELECT '管理员', 'ROLE_ADMIN', '系统管理员'
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE code = 'ROLE_ADMIN');

INSERT INTO sys_role (name, code, description)
SELECT '普通用户', 'ROLE_USER', '普通用户'
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE code = 'ROLE_USER');

-- 插入管理员用户（密码：admin123）
INSERT INTO sys_user (username, password, full_name, email, status)
SELECT 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 'admin@example.com', 1
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'admin');

-- 给管理员分配角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id
FROM sys_user u, sys_role r
WHERE u.username = 'admin' AND r.code = 'ROLE_ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM sys_user_role ur
    WHERE ur.user_id = u.id AND ur.role_id = r.id
); 