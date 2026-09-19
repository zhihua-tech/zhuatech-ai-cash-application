/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicashapplication.domain;
import org.springframework.stereotype.Component;
import java.util.*;
@Component
public class DomainCatalog {
    private final Map<String, WorkflowAction> actions = new LinkedHashMap<>();
    public DomainCatalog() {
        actions.put("MATCH", new WorkflowAction("MATCH", "提交智能匹配", List.of("草稿"), "待复核", "OPERATOR"));
        actions.put("APPROVE", new WorkflowAction("APPROVE", "批准核销方案", List.of("待复核"), "待过账", "ADMIN"));
        actions.put("POST", new WorkflowAction("POST", "确认财务过账", List.of("待过账"), "已核销", "ADMIN"));
    }
    public String systemName() { return "知华科技AI智能收款核销系统"; }
    public String scene() { return "银行流水、客户识别、开放发票、智能匹配、差异处理、核销审批、退款与审计"; }
    public String initialStatus() { return "草稿"; }
    public String partyLabel() { return "客户/收款/发票"; }
    public String amountLabel() { return "到账与核销金额"; }
    public String quantityLabel() { return "收款笔数"; }
    public String dueLabel() { return "核销期限"; }
    public List<ModuleDefinition> modules() { return List.of(
            new ModuleDefinition("BANK_FEED", "银行流水", "接收银行回单、虚拟账户和支付平台到账记录"),
            new ModuleDefinition("CUSTOMER_RESOLUTION", "AI客户识别", "综合户名、附言、账号和历史行为识别付款方"),
            new ModuleDefinition("RECEIVABLE", "应收发票", "同步开放发票、贷项、账龄和争议状态"),
            new ModuleDefinition("MATCHING", "智能核销", "执行一对一、一对多、多对一和组合匹配"),
            new ModuleDefinition("CONFIDENCE", "置信度与解释", "展示金额、参考号、日期和客户证据贡献"),
            new ModuleDefinition("EXCEPTION", "差异处理", "处理短款、长款、手续费、重复到账和未知款"),
            new ModuleDefinition("APPROVAL", "核销审批", "按金额、置信度和差异执行职责分离审批"),
            new ModuleDefinition("REFUND", "退款与转款", "控制退款、跨客户转款和账户验证"),
            new ModuleDefinition("AUDIT", "资金审计", "保留原始流水、模型版本、人工改写和过账回执")
        ); }
    public Map<String, WorkflowAction> actions() { return Collections.unmodifiableMap(actions); }
    public record ModuleDefinition(String code,String name,String description) {}
    public record WorkflowAction(String code,String label,List<String> from,String to,String requiredRole) {}
}
