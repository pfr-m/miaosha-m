既然要上传到 GitHub，一个高标准的 **README.md** 不仅能让你的项目看起来更专业，还能在面试官点开你的主页时，一眼看出你对高并发场景的理解。

建议将包名改为 **`com.miaosha.project`** 或 **`com.github.miaosha`**。同时，README 中我们要突出 **分布式 Session**、**JSR303 校验** 和 **Redis 优化** 等技术点。

你可以直接复制以下内容到你项目根目录的 `README.md` 文件中：

------

# 🚀 高并发秒杀管理系统 (Miaosha-M)

本项目是一个基于 **Spring Boot 4.0 (Jakarta EE)** 和 **Redis** 实现的高并发秒杀平台。核心目标是解决秒杀场景下的**高并发接入**、**分布式 Session 共享**以及**数据一致性**问题。

## 🛠️ 技术栈

- **核心框架**：Spring Boot 4.0.3 (最新 Jakarta EE 规范)
- **后端开发**：Java 21, Lombok (简化代码)
- **持久层**：MyBatis + MySQL
- **缓存/并发控制**：Redis (用于分布式 Session 存储及数据预热)
- **前端交互**：Thymeleaf 模板引擎 + JQuery + MD5 加密
- **安全校验**：JSR303 参数校验 (Jakarta Validation)

------

## 🌟 核心功能与亮点

### 1. 分布式 Session 架构

- **现状**：传统 Session 无法满足集群部署需求。
- **方案**：自定义 `UserArgumentResolver` 参数解析器。
- **效果**：通过 Cookie 携带 Token，后端从 Redis 动态解析用户信息并自动注入 Controller 参数，实现无状态化的水平扩展。

### 2. 多重加密与安全防护

- **前端加密**：用户密码在前端经过 MD5 一次加密，防止明文传输。
- **后端加盐**：后端获取加密后的密文，结合数据库随机盐值（Salt）进行二次 MD5，确保数据库即使泄露也无法还原密码。

### 3. 全局异常处理机制

- 通过 `@ControllerAdvice` 实现 `GlobalExceptionHandler`。
- 统一拦截业务异常（`GlobalException`）与 JSR303 校验异常，确保前端收到的永远是规范的 `Result<T>` 格式。

### 4. 高性能商品列表

- 采用 **MyBatis 关联查询**，将通用商品表（`goods`）与秒杀专有表（`miaosha_goods`）进行 `LEFT JOIN`。
- 结合 Redis 缓存预热，大幅降低数据库查询压力。

------

## 🏗️ 快速开始

1. **环境准备**：JDK 21+, Maven 3.x, Redis 7.x, MySQL 8.x。
2. **修改配置**：在 `src/main/resources/application.properties` 中修改你的数据库与 Redis 连接地址。
3. **启动**：运行 `MiaoshaMApplication`。
4. **访问**：`http://localhost:8088/login/to_login`。

------

## 📂 项目结构

Plaintext

```
src/main/java/com/miaosha/m
├── controller/     # 登录与商品逻辑
├── service/        # 核心业务，包含分布式Session处理
├── mapper/         # 数据库访问层
├── entity/         # 数据库映射对象
├── vo/             # 数据传输对象 (LoginVo, GoodsVo)
├── redis/          # Redis通用前缀及操作封装
├── exception/      # 全局异常体系
└── config/         # 包含WebMvc与参数解析器配置
```

