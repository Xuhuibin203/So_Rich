# So Rich 大富翁 — 项目开发日志

> 在线多人古风大富翁游戏，唐代主题风格，支持实时对战。

---

## 一、项目概况

| 项 | 详情 |
|----|------|
| 项目名 | So Rich（大富翁） |
| 主题风格 | 古风唐代、高级艺术感 |
| 仓库地址 | https://github.com/Xuhuibin203/So_Rich.git |
| 开始时间 | 2026-06-30 |

---

## 二、技术选型

### 前端
| 技术 | 版本/说明 |
|------|-----------|
| Vue 3 | Composition API + `<script setup>` |
| TypeScript | 严格模式 |
| Vite | 开发服务器 + 构建 |
| Pinia | 状态管理 |
| Vue Router | 路由（/ → /lobby → /game/:roomId） |
| ~~PixiJS~~ | 不需要。棋盘类游戏用 DOM + CSS 动画足够 |

### 后端
| 技术 | 版本/说明 |
|------|-----------|
| Spring Boot | **4.1.0**（最新版） |
| JDK | 17 |
| MySQL | 8.0，数据库名 `so_rich` |
| MyBatis-Plus | 3.5.10（`mybatis-plus-spring-boot3-starter`） |
| Redis | 本地 Windows 安装 |
| WebSocket | Spring 原生 WebSocket（`/ws/game/{roomId}/{playerId}`） |
| Fastjson2 | 2.0.56 |
| Lombok | ✅（注意限制，见下文） |

### 工具链
| 工具 | 说明 |
|------|------|
| Maven Wrapper | `mvnw.cmd`，无需本地安装 Maven |
| Git | 2.54.0，SSH 认证 |
| 本地环境 | Windows 11 |
| 服务器 | 阿里云轻量服务器 + 宝塔面板（待部署） |

---

## 三、已完成事项

### 3.1 前端初始化
- ✅ Vite + Vue 3 + TypeScript 项目骨架
- ✅ Vue Router 配置（首页 → 大厅 → 游戏）
- ✅ Vite 开发代理配置（`/api` → 8080，`/ws` → WebSocket）
- ✅ 三个占位页面：Home / Lobby / Game

### 3.2 后端初始化
- ✅ Spring Boot 4.1.0 项目骨架
- ✅ WebSocket 配置（`WebSocketConfig` + `GameWebSocketHandler`）
- ✅ 统一响应类 `Result<T>`
- ✅ MySQL + Redis + MyBatis-Plus 配置

### 3.3 Redis 安装
- ✅ 本地 Windows 安装 Redis 服务
- ✅ 安装路径：`E:\software\redis\`
- ✅ 服务状态：Running，`redis-cli ping` → PONG

### 3.4 数据库设计
- ✅ 5 张数据表设计文档（`docs/db/`）
- ✅ SQL 建表脚本（`resources/db/init.sql`）
- ✅ 5 个 Entity 实体类（`entity/`）
- ✅ 5 个 Mapper 接口（`mapper/`）

### 3.5 Git 配置
- ✅ 仓库初始化
- ✅ 远程仓库关联
- ✅ SSH Key 生成并添加到 GitHub
- ✅ 无 Token 明文存储，纯 SSH 认证
- ✅ 代码已推送到 GitHub

---

## 四、数据表设计

| 序号 | 表名 | 用途 |
|------|------|------|
| 1 | `user` | 用户登录认证、昵称 |
| 2 | `room` | 游戏房间管理（WAITING/PLAYING/FINISHED） |
| 3 | `room_player` | 每局玩家金钱、位置、回合顺序、状态 |
| 4 | `board_cell` | 棋盘40格静态配置（名称/价格/租金梯度） |
| 5 | `player_property` | 玩家购地、建房、抵押记录 |

表关系：
```
user ──┐
       ├── room_player ── room
       │       │
       │       └── player_property ── board_cell
       │
       └── room（房主）
```

---

## 五、遇到的问题与解决方案

### 问题 1：Lombok @AllArgsConstructor 泛型编译失败

**现象：**
```java
@Data
@AllArgsConstructor
public class Result<T> {
    // ...
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);  // ❌ 编译报错
    }
}
```
错误：`无法推断类型变量 T`

**根因：** Lombok 的 `@AllArgsConstructor` 在泛型类中生成的构造器，与 Java 编译器的钻石操作符 `<>` 结合 `null` 参数时，无法完成类型推断。与 Spring Boot 版本无关。

**解决：**
- **去掉 `@AllArgsConstructor`**，手写全参构造器（仅 4 行代码）
- **保留 `@Data` + `@NoArgsConstructor`**，完全不受影响
- 在静态工厂方法中对 `null` 使用显式类型参数：`new Result<T>(200, "success", null)`

### 问题 2：Spring Boot 4.x 依赖命名变更

**现象：** 从 4.1.0 降级到 3.4.1 时依赖解析失败。

**根因：** Spring Boot 4.x 将 `spring-boot-starter-web` 重命名为 `spring-boot-starter-webmvc`。

**解决：** 使用 4.1.0 时，artifactId 必须写 `spring-boot-starter-webmvc`。

### 问题 3：GitHub PAT Token 过期 → 推送 403

**现象：** `git push` 返回 `403 Permission denied`。

**根因：** 之前嵌入在远程 URL 中的 GitHub Personal Access Token 已过期。

**解决：**
1. GitHub 生成新 Token（勾选 `repo` 权限）
2. 更新远程 URL 中的 Token
3. **长期方案：切换到 SSH Key 认证**，彻底消灭 Token

### 问题 4：GitHub Push Protection 拦截（历史提交含旧 Token）

**现象：** 推送被 GitHub 拒绝，提示历史 commit 中含有 Secret。

**解决：** 删除 `.git` 目录重建仓库，将所有文件重新提交为一个干净的初始 commit，然后 force push。

### 问题 5：Windows 沙箱环境 SSH Push 失败

**现象：** 沙箱中 `git push` 报 `Permission denied (publickey)`，路径乱码。

**根因：** 沙箱的 Home 目录与用户真实 Home 目录不同，SSH 找不到私钥。

**解决：** 设置环境变量显式指定 SSH 密钥路径：
```powershell
$env:GIT_SSH_COMMAND = 'ssh -i C:/Users/许謹/.ssh/id_ed25519 -o StrictHostKeyChecking=accept-new'
```
用户本地终端直接 `git push` 无此问题。

---

## 六、注意事项

1. **Lombok 使用规范：** 泛型类中禁用 `@AllArgsConstructor`，改用 `@Data` + `@NoArgsConstructor` + 手写构造器
2. **Spring Boot 4.1.0：** Web 模块 artifactId 是 `spring-boot-starter-webmvc`，不是 `-web`
3. **Maven 构建：** 项目使用 Maven Wrapper（`mvnw.cmd`），不要直接用 `mvn`
4. **Redis 连接：** 本地 `redis-cli` 未加入 PATH，调试时用完整路径 `E:\software\redis\redis-cli.exe`
5. **Git 认证：** 项目使用 SSH Key，远程地址为 `git@github.com:Xuhuibin203/So_Rich.git`
6. **古风主题：** 只影响前端视觉（字体/样式/棋盘名称），数据库 schema 不变
