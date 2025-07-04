# Vben Admin Mock API 文档

## 目录
1. [认证相关](#认证相关)
2. [菜单相关](#菜单相关)
3. [用户管理](#用户管理)
4. [系统管理](#系统管理)
5. [文件上传](#文件上传)
6. [表格数据](#表格数据)

## 认证相关

### POST /api/auth/login
**描述**：用户登录接口

**请求参数**：
```json
{
  "username": "string",
  "password": "string"
}
```

**请求示例**：
```bash
curl -X POST http://localhost:5320/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

**成功响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "管理员",
    "avatar": "",
    "roles": ["admin"],
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

**错误响应**：
- 400 Bad Request
  ```json
  {
    "code": 400,
    "message": "Username and password are required"
  }
  ```
- 403 Forbidden
  ```json
  {
    "code": 403,
    "message": "Username or password is incorrect"
  }
  ```

### POST /api/auth/logout
**描述**：用户登出

**请求头**：
```
Authorization: Bearer {accessToken}
```

**响应**：
```json
{
  "code": 200,
  "message": "success"
}
```

## 菜单相关

### GET /api/menu/all
**描述**：获取用户菜单

**请求头**：
```
Authorization: Bearer {accessToken}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "path": "/dashboard",
      "name": "Dashboard",
      "icon": "ion:grid-outline",
      "children": [
        {
          "path": "/dashboard/analysis",
          "name": "Analysis"
        }
      ]
    }
  ]
}
```

## 用户管理

### GET /api/user/info
**描述**：获取当前用户信息

**请求头**：
```
Authorization: Bearer {accessToken}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "管理员",
    "avatar": "",
    "roles": ["admin"]
  }
}
```

## 系统管理

### GET /api/system/dept/list
**描述**：获取部门列表

**请求头**：
```
Authorization: Bearer {accessToken}
```

### GET /api/system/menu/list
**描述**：获取系统菜单列表

**请求头**：
```
Authorization: Bearer {accessToken}
```

### GET /api/system/role/list
**描述**：获取角色列表

**请求头**：
```
Authorization: Bearer {accessToken}
```

## 文件上传

### POST /api/upload
**描述**：文件上传接口

**请求头**：
```
Authorization: Bearer {accessToken}
Content-Type: multipart/form-data
```

**请求参数**：
- file: 上传的文件

**响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "url": "/uploads/xxx.jpg"
  }
}
```

## 表格数据

### GET /api/table/list
**描述**：获取表格数据

**请求头**：
```
Authorization: Bearer {accessToken}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "items": [
      {
        "id": 1,
        "name": "John Brown",
        "age": 32,
        "address": "New York No. 1 Lake Park"
      }
    ],
    "total": 1
  }
}
```

## 注意事项
1. 所有API基础路径为：`http://localhost:5320`
2. 需要认证的API必须在请求头中添加：`Authorization: Bearer {accessToken}`
3. 服务端口可在`.env`文件中修改
4. 所有数据均为模拟数据，仅用于开发测试
