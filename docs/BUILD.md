# 构建说明

## 环境要求

- JDK 17（MC 1.20.1 + Fabric Loom 1.7 要求）
- 无需预装 Gradle，使用仓库自带的 wrapper（Gradle 8.10）

## 构建

```bash
# 仓库根目录
./gradlew :steelcloud-core:build
```

产物位于 `steelcloud-core/build/libs/`：

- `steelcloud-core-<version>.jar`（remap 后的模组 jar）
- `steelcloud-core-<version>-sources.jar`

## 常见问题

### `./gradlew: Permission denied`

`gradlew` 在 git 索引中必须是 `100755`。检查：

```bash
git ls-files -s gradlew   # 期望输出以 100755 开头
```

若显示 `100644`，修复：

```bash
git update-index --chmod=+x gradlew
git commit -m "chore(build): make gradlew executable"
```

临时绕过（不推荐，仅用于本地排查）：

```bash
sh gradlew :steelcloud-core:build
```

### `Plugin [id: 'fabric-loom'] was not found`

根 `settings.gradle` 必须包含 `pluginManagement` 块并声明 Fabric maven。子模块自带的 `settings.gradle` 在根构建中不生效，不能依赖它。

### `No matching variant ... plugin api-version 8.8`

wrapper 的 Gradle 版本低于 Loom 1.7 的要求（≥ 8.8）。当前使用 8.10，勿降级到 8.5。
