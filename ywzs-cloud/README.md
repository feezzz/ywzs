# 运维值守系统 - 后端

基于 Spring Boot 的运维值守系统后端项目。

## 技术栈

- Spring Boot 2.7.18
- Spring Security
- JWT 认证
- Spring Data JPA
- MySQL 数据库
- Maven

## 项目结构

```
src/main/java/org/example/ywzscloud/
├── config/         # 配置类
│   ├── SecurityConfig.java    # 安全配置
│   └── WebConfig.java        # Web配置
├── controller/     # 控制器
│   ├── AuthController.java   # 认证相关
│   └── DutyController.java   # 值班相关
├── dto/           # 数据传输对象
│   ├── LoginRequest.java
│   └── LoginResponse.java
├── entity/        # 实体类
│   ├── User.java
│   └── Duty.java
├── repository/    # 数据访问层
│   ├── UserRepository.java
│   └── DutyRepository.java
├── security/      # 安全相关
│   └── JwtTokenProvider.java
├── service/       # 业务逻辑层
│   ├── UserService.java
│   └── DutyService.java
└── YwzsCloudApplication.java  # 启动类
```

## 开发环境

- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+

## 快速开始

1. 创建数据库
```sql
CREATE DATABASE ywzs_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 配置数据库连接
```properties
# src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/ywzs_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. 配置 JWT
```properties
# JWT Configuration
app.jwt.secret=your-secret-key
app.jwt.expiration=86400000
```

4. 运行应用
```bash
mvn spring-boot:run
```

## API 文档

### 认证接口

#### 登录
```
POST /api/auth/login
Content-Type: application/json

{
    "username": "admin",
    "password": "password"
}
```

响应：
```json
{
    "token": "jwt_token",
    "userId": 1,
    "username": "admin"
}
```

#### 获取用户信息
```
GET /api/auth/user-info
Authorization: Bearer jwt_token
```

### 值班管理接口

#### 获取值班统计
```
GET /api/duty/dashboard/stats
Authorization: Bearer jwt_token
```

#### 获取值班日历
```
GET /api/duty/calendar?year=2024&month=3
Authorization: Bearer jwt_token
```

## 数据库设计

### 用户表 (users)
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50),
    email VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 值班记录表 (duty_records)
```sql
CREATE TABLE duty_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    duty_date DATE NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    status VARCHAR(20) NOT NULL,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## 开发指南

### 添加新功能

1. 创建实体类
```java
@Entity
@Table(name = "table_name")
public class EntityName {
    // 属性定义
}
```

2. 创建 Repository
```java
@Repository
public interface EntityRepository extends JpaRepository<Entity, Long> {
    // 自定义查询方法
}
```

3. 创建 Service
```java
@Service
public class EntityService {
    @Autowired
    private EntityRepository repository;
    
    // 业务方法
}
```

4. 创建 Controller
```java
@RestController
@RequestMapping("/api/path")
public class EntityController {
    @Autowired
    private EntityService service;
    
    // API 端点
}
```

### 安全配置

在 `SecurityConfig.java` 中配置：
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .authorizeRequests()
        .antMatchers("/api/auth/**").permitAll()
        .anyRequest().authenticated();
}
```

## 部署

1. 打包
```bash
mvn clean package
```

2. 运行
```bash
java -jar target/ywzs-cloud-1.0-SNAPSHOT.jar
```

## 性能优化

1. 数据库优化
- 添加适当的索引
- 优化查询语句
- 使用缓存

2. JVM 调优
```bash
java -Xms512m -Xmx1024m -jar target/ywzs-cloud-1.0-SNAPSHOT.jar
```

## 监控

1. 使用 Spring Boot Actuator
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

2. 配置 Actuator 端点
```properties
management.endpoints.web.exposure.include=health,metrics,info
```

## 更新日志

### v1.0.0
- 初始版本
- 基础功能实现 