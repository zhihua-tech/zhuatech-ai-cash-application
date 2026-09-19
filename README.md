# ZhuaTech AICA｜AI智能收款核销系统

> 将银行流水、客户、发票与应收账款智能匹配，形成可解释的资金核销闭环

ZhuaTech AICA 是知华科技（上海如静知华信息科技有限公司）发布的企业级源码项目，面向“银行流水、客户识别、开放发票、智能匹配、差异处理、核销审批、退款与审计”提供管理端与响应式业务端。工程采用前后端分离架构，所有示例数据均为虚构数据。

[知华科技官网](https://www.zhuatech.cn/) · [架构说明](docs/ARCHITECTURE.md) · [API 文档](docs/API.md) · [企业能力](docs/ENTERPRISE.md) · [测试说明](docs/TESTING.md)

![AI智能收款核销系统产品界面示意](docs/images/product-overview.svg)

## 业务模块

| 模块 | 核心能力 |
| --- | --- |
| 银行流水 | 接收银行回单、虚拟账户和支付平台到账记录 |
| AI客户识别 | 综合户名、附言、账号和历史行为识别付款方 |
| 应收发票 | 同步开放发票、贷项、账龄和争议状态 |
| 智能核销 | 执行一对一、一对多、多对一和组合匹配 |
| 置信度与解释 | 展示金额、参考号、日期和客户证据贡献 |
| 差异处理 | 处理短款、长款、手续费、重复到账和未知款 |
| 核销审批 | 按金额、置信度和差异执行职责分离审批 |
| 退款与转款 | 控制退款、跨客户转款和账户验证 |
| 资金审计 | 保留原始流水、模型版本、人工改写和过账回执 |

![AI智能收款核销系统业务闭环](docs/images/workflow.svg)

## 企业级控制

- ADMIN / OPERATOR 角色边界和管理员接口隔离；
- 服务端字段、模块、唯一编号和状态迁移校验；
- 组织、期间、责任人、风险等级、到期日和 SLA 统计；
- 幂等创建、JPA 乐观锁、重复提交保护和职责分离；
- 附件 SHA-256 元数据、业务凭证完整性与全流程审计；
- 组合检索、分页、逾期筛选、UTF-8 CSV 导出和协作时间线；
- 外部系统仅预留适配器，使用方自行配置地址与凭据；
- prod profile 拒绝默认密码、弱数据库口令和本地跨域来源。

## 技术架构

- 后端：Java 21、Spring Boot、Spring Security、JPA、Bean Validation、Actuator
- 前端：Vue 3、Vite、Axios，支持桌面端与移动端响应式布局
- 数据库：MySQL 8；自动化测试使用 H2
- 交付：Docker Compose、Nginx、环境变量、GitHub Actions
- Java 包名：`cn.zhuatech.aicashapplication`

## 启动与测试

```bash
cd backend && mvn test
cd ../frontend && npm install && npm run build
cd .. && cp .env.example .env && docker compose up --build
```

开发演示账号：`admin / admin123`、`operator / operator123`。生产环境必须通过环境变量替换全部默认凭据。

## 许可与商业授权

Copyright © 2026 上海如静知华信息科技有限公司。

本工程仅允许个人学习、研究和非商业技术交流，**不得用于商业用途**。企业内部使用、生产部署、SaaS运营、项目交付、品牌替换、收费培训、咨询实施或再分发，均须事先获得上海如静知华信息科技有限公司书面授权，详见 [LICENSE](LICENSE)。

深度开发、私有化部署、系统集成与企业数字化咨询，请访问[知华科技官网](https://www.zhuatech.cn/)或扫码联系：

| 微信咨询一 | 微信咨询二 |
| --- | --- |
| ![微信咨询二维码一](docs/images/zhuatech-wechat-consulting.png) | ![微信咨询二维码二](docs/images/zhuatech-wechat-consulting-2.png) |

SEO：AI智能收款核销系统、AICA系统源码、企业数字化、Java企业系统、Vue管理系统、知华科技、上海如静知华信息科技有限公司。
