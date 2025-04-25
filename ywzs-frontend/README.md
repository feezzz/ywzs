# 运维值守系统 - 前端

基于 Vue 3 + Element Plus 的运维值守系统前端项目。

## 技术栈

- Vue 3
- Vuex 4
- Vue Router 4
- Element Plus
- Axios

## 项目结构

```
src/
├── api/          # API 接口
│   ├── auth.js   # 认证相关
│   └── duty.js   # 值班相关
├── views/        # 页面组件
│   ├── Login.vue # 登录页
│   ├── Layout.vue # 主布局
│   └── Dashboard.vue # 仪表盘
├── store/        # Vuex 状态管理
│   └── index.js
├── router/       # 路由配置
│   └── index.js
├── App.vue       # 根组件
└── main.js       # 入口文件
```

## 开发环境

- Node.js 14+
- npm 6+ 或 yarn

## 快速开始

1. 安装依赖
```bash
npm install
```

2. 创建环境配置文件 `.env`
```
VUE_APP_API_URL=http://localhost:8080/api
VUE_APP_API_TIMEOUT=5000
```

3. 启动开发服务器
```bash
npm run serve
```

4. 构建生产版本
```bash
npm run build
```

## 功能模块

### 1. 认证模块
- 登录/注销
- JWT token 管理
- 路由权限控制

### 2. 用户管理
- 用户列表
- 角色管理
- 组织管理

### 3. 值班管理
- 值班日历
- 交接班记录
- 值班统计

### 4. 报表管理
- 值班报表
- 数据统计

## 开发指南

### 添加新页面

1. 在 `views/` 下创建页面组件
2. 在 `router/index.js` 添加路由配置
3. 在 `api/` 下添加相关接口
4. 如需要，在 `store/` 下添加状态管理

### 状态管理

使用 Vuex 管理全局状态：
- `state`: 状态定义
- `mutations`: 同步修改状态
- `actions`: 异步操作
- `getters`: 计算属性

### API 调用

使用封装的 axios 实例：
```javascript
import api from '@/api/auth'

// 调用示例
const response = await api.get('/endpoint')
```

## 部署

1. 修改生产环境配置 `.env.production`
```
VUE_APP_API_URL=https://api.example.com
```

2. 构建
```bash
npm run build
```

3. 部署 `dist/` 目录到 Web 服务器

## 代码规范

- 使用 ESLint 进行代码检查
- 遵循 Vue 官方风格指南
- 组件名使用 PascalCase
- Props 定义要详细
- 使用 Composition API

## 常见问题

1. 跨域问题
- 检查 API 地址配置
- 确认后端 CORS 配置

2. 路由问题
- 检查权限配置
- 确认路由定义正确

## 更新日志

### v1.0.0
- 初始版本
- 基础功能实现 