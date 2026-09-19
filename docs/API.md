# AI智能收款核销系统 API

所有业务接口默认位于 `/api`，除 `/public/**` 和健康检查外均需要 HTTP Basic 身份认证。生产环境应接入企业 IAM 或统一身份平台。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/public/about` | 产品、公司、官网和许可元数据 |
| GET | `/catalog` | 业务模块、字段标签和状态动作 |
| GET | `/dashboard` | 业务规模、金额、状态和模块统计 |
| GET/POST | `/records` | 业务台账查询与创建 |
| GET/PUT/DELETE | `/records/{id}` | 详情、草稿修改与删除 |
| POST | `/records/{id}/actions` | 执行服务端状态迁移 |
| POST | `/records/{id}/comments` | 增加协作记录 |
| GET | `/records/{id}/timeline` | 查询完整操作时间线 |
| GET | `/records/search` | 组合检索、分页和逾期筛选 |
| GET | `/records/export.csv` | 导出 UTF-8 CSV |
| GET | `/sla-summary` | SLA、逾期、风险和人员工作量 |
| POST | `/domain/decision` | 执行AI智能收款核销系统专属领域规则 |
| POST | `/domain/cash-allocation` | 多发票到账分配，返回核销行、置信度、剩余应收和未认领预警 |
| GET/POST | `/enterprise/controls` | 企业控制项查询与幂等创建 |
| POST | `/enterprise/controls/{id}/submit` | 提交复核 |
| POST | `/admin/enterprise/controls/{id}/review` | 管理员审批或驳回 |
| POST | `/enterprise/controls/{id}/documents` | 登记附件哈希及存储元数据 |
| POST | `/enterprise/controls/{id}/complete` | 凭证完整后办结 |
| POST | `/admin/enterprise/controls/{id}/sync` | 登记外部系统回执 |

## 自动核销分配

`/domain/cash-allocation` 接收银行流水号、客户编码、到账金额、容差和候选发票列表。候选发票包含发票号、客户、开放金额、到期日及附言是否命中；服务端校验重复发票并按“附言命中优先、到期日优先”分配。

## 领域决策字段

| 字段 | 类型 | 含义 |
| --- | --- | --- |
| `bankTransactionNo` | String | 银行流水号 |
| `receiptAmount` | double | 到账金额 |
| `invoiceAmount` | double | 候选发票金额 |
| `referenceSimilarity` | double | 附言相似度(%) |
| `customerConfidence` | double | 客户识别置信度(%) |
| `dateDifferenceDays` | int | 到账与发票日期差(天) |
| `duplicateReceipt` | boolean | 检测到重复到账 |
| `bankAccountVerified` | boolean | 付款账户已验证 |
| `disputeOpen` | boolean | 存在未关闭争议 |

接口统一返回 `ApiResponse`；业务冲突使用 HTTP 409，参数错误使用 400，未认证使用 401，无权限使用 403。
