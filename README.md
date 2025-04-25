# 运维值守系统

运维值守系统是一个基于 Spring Boot + Vue3 的全栈应用，用于管理运维团队的值班、交接班等日常工作。

## 项目架构

### 技术栈

#### 后端 (ywzs-cloud)
- Spring Boot 2.7.18
- Spring Security
- JWT 认证
- Spring Data JPA
- MySQL 数据库
- Maven

#### 前端 (ywzs-frontend)
- Vue 3
- Vuex 4
- Vue Router 4
- Element Plus
- Axios

### 项目结构

```
ywzs/
├── ywzs-cloud/          # 后端项目
│   ├── src/main/java/org/example/ywzscloud/
│   │   ├── config/      # 配置类
│   │   ├── controller/  # 控制器层
│   │   ├── dto/         # 数据传输对象
│   │   ├── entity/      # 实体类
│   │   ├── repository/  # 数据访问层
│   │   ├── security/    # 安全相关配置
│   │   ├── service/     # 业务逻辑层
│   │   └── YwzsCloudApplication.java
│   └── pom.xml
│
├── ywzs-frontend/       # 前端项目
│   ├── src/
│   │   ├── api/        # API 接口
│   │   ├── views/      # 页面组件
│   │   ├── store/      # Vuex 状态管理
│   │   ├── router/     # 路由配置
│   │   ├── App.vue     # 根组件
│   │   └── main.js     # 入口文件
│   └── package.json
```

## 功能模块

### 1. 认证模块
- 用户登录/注销
- JWT token 认证
- 权限控制

### 2. 用户管理
- 用户 CRUD
- 角色权限管理
- 组织架构管理

### 3. 值班管理
- 值班排班
- 交接班管理
- 值班记录

### 4. 报表管理
- 值班统计
- 报表生成

## 开发环境搭建

### 后端环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+

### 前端环境要求
- Node.js 14+
- npm 6+ 或 yarn

### 后端启动步骤

1. 配置数据库
```sql
create database ywzs_db;
```

2. 修改配置文件
```properties
# src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/ywzs_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. 启动应用
```bash
cd ywzs-cloud
mvn spring-boot:run
```

### 前端启动步骤

1. 安装依赖
```bash
cd ywzs-frontend
npm install
```

2. 配置环境变量
```bash
# .env
VUE_APP_API_URL=http://localhost:8080/api
VUE_APP_API_TIMEOUT=5000
```

3. 启动开发服务器
```bash
npm run serve
```

## API 文档

### 认证接口

#### 登录
- POST /api/auth/login
```json
{
  "username": "string",
  "password": "string"
}
```

#### 获取用户信息
- GET /api/auth/user-info

#### 注销
- POST /api/auth/logout

### 值班管理接口

#### 获取值班统计
- GET /api/duty/dashboard/stats

#### 获取值班日历
- GET /api/duty/calendar?year=2024&month=3

#### 获取值班通知
- GET /api/duty/notices

#### 获取交接班记录
- GET /api/duty/handovers/recent

## 部署指南

### 后端部署
1. 打包
```bash
cd ywzs-cloud
mvn clean package
```

2. 运行
```bash
java -jar target/ywzs-cloud-1.0-SNAPSHOT.jar
```

### 前端部署
1. 构建
```bash
cd ywzs-frontend
npm run build
```

2. 部署 dist 目录到 Web 服务器

## 开发规范

### Git 提交规范
```
feat: 新功能
fix: 修复问题
docs: 文档修改
style: 代码格式修改
refactor: 代码重构
test: 测试用例修改
chore: 其他修改
```

### 代码风格
- 后端遵循 Alibaba Java 编码规范
- 前端遵循 Vue 官方风格指南

## 常见问题

1. 跨域问题
- 确保后端 CORS 配置正确
- 检查前端 API 请求地址配置

2. 登录问题
- 检查 token 是否正确存储
- 确认后端 JWT 配置正确

## 维护者

- [维护者姓名]
- [联系方式]

## 许可证

[许可证类型] 