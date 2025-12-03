# Quarkus-Vue-Admin

![build](https://img.shields.io/badge/build-passing-brightgreen?style=for-the-badge)
![license](https://img.shields.io/github/license/zpfz/vuepress-theme-antdocs?style=for-the-badge)

<div class="reset-mobile-brand">
    <img width="130" src="https://avatars.githubusercontent.com/u/47638783?s=200&v=4">
    <span class="sign"> +</span>
    <img width="130" src="https://cn.vuejs.org/images/logo.png">
</div>

<p>&nbsp; </p>

<a-alert type="info" showIcon>
  <span slot="description">
    Quarkus-Vue-Admin是一款基于Quarkus+Vue+AntDesignVue开发的后台管理系统快速开发脚手架
  </span>
</a-alert>

- 功能演示：~~https://whatever-team.github.io/quarkus-vue-admin admin/888888~~
- 开发文档：https://whatever-team.github.io/quarkus-vue-admin-docs/

## 主要功能

- 权限系统：该有的都有
- 文件上传下载：就是上传和下载
- 字典封装：前端使用mixins封装字典函数，试试看
- 参数管理：页面上直接编辑
- 操作日志：自动记录存储，方便问题定位
- DevUI：可视化展示所有装配对象，在页面上修改配置文件
- 更多示例：基础的增删改查、任务TODO列表等更多示例
- 代码生成器：后台基础逻辑以及简单curd页面的代码生成器

## 技术栈

- 采用[Quarkus](https://quarkus.io/)作为后端框架，配合GraalVM食用更佳
- 前端使用基于[Vue](https://cn.vuejs.org/)和[AntDesignVue](https://cn.vuejs.org/)的[Vue Antd Admin](https://github.com/iczer/vue-antd-admin)
- 数据库：采用MySql，使用JPA实现对数据库的增删改查操作
- API文档：使用Swagger构建自动化文档

## 如何贡献

如果你希望参与贡献，欢迎 [Pull Request](https://github.com/whatever-team/quarkus-vue-admin/pulls)，或给我们 [报告 Bug](https://github.com/whatever-team/quarkus-vue-admin/issues) 。

> 强烈推荐阅读 [《如何有效地报告 Bug》](https://www.chiark.greenend.org.uk/~sgtatham/bugs-cn.html)，更好的问题更容易获得帮助。

## 当前子项目与版本（2025-12）

- qva-server：Java 17 + Quarkus 3.30.1，依赖 hutool 5.8.42、Lombok 1.18.42、Freemarker 2.3.34、EasyExcel 4.0.3。
- qva-web：Vue 3.5 + Vite 7 + Arco Design，Node ≥ 18，主要依赖 axios 1.13.x、pinia 3.x、vue-router 4.6.x。
- web（legacy）：Vue 2.7 + Vue CLI 5 + Ant Design Vue 1.7.x，Node ≥ 14，适合兼容性场景。

> 提示：升级后需要使用新的 Node 版本（建议 18+）并重新安装依赖（pnpm i）。

## 待补充功能（对标主流管理后台脚手架）

- 多租户与数据隔离：租户粒度的库/表隔离及资源配额。
- 行/字段级数据权限：结合角色/部门/用户维度的细粒度访问控制。
- 统一登录与 SSO：OIDC/SAML/LDAP、企业微信/钉钉/飞书 等三方登录。
- 配置中心与灰度开关：远程配置、特性开关、动态路由/菜单下发。
- 观测与稳定性：Prometheus 指标、分布式追踪、集中日志。
- 流程/表单编排：可视化流程（BPMN）、表单/页面设计器与低代码生成。
- 自动化测试基线：前后端单元测试 + E2E 测试脚手架。
- CI/CD 与部署模板：镜像构建、Helm/K8s/Docker Compose 示例和质量检查流水线。


