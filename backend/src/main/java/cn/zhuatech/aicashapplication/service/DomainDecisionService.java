/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicashapplication.service;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service public class DomainDecisionService {
 public DecisionResult assess(DecisionRequest request) { double variance=Math.abs(request.receiptAmount()-request.invoiceAmount());double tolerance=Math.max(1,request.receiptAmount()*0.001);int score=(int)Math.round((request.referenceSimilarity()+request.customerConfidence())/2);List<String>actions=new ArrayList<>();if(variance>tolerance){score-=35;actions.add("处理到账与发票金额差异");}if(request.dateDifferenceDays()>30){score-=15;actions.add("复核跨期到账与发票关系");}if(request.duplicateReceipt()){score-=70;actions.add("冻结重复到账并核验银行流水唯一性");}if(!request.bankAccountVerified()){score-=45;actions.add("验证付款账户与客户关系");}if(request.disputeOpen()){score-=40;actions.add("争议关闭前禁止自动核销");}return result(score,actions,"AUTO_APPLY","MANUAL_REVIEW","BLOCKED",Map.of("amountVariance",variance,"tolerance",tolerance,"referenceSimilarity",request.referenceSimilarity(),"customerConfidence",request.customerConfidence())); }
 private DecisionResult result(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=80?good:score>=50?warn:bad;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 private DecisionResult riskResult(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=70?bad:score>=40?warn:good;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 public record DecisionRequest(
        @NotBlank String bankTransactionNo,
        @Positive double receiptAmount,
        @Positive double invoiceAmount,
        @DecimalMin("0") @DecimalMax("100") double referenceSimilarity,
        @DecimalMin("0") @DecimalMax("100") double customerConfidence,
        @PositiveOrZero int dateDifferenceDays,
        boolean duplicateReceipt,
        boolean bankAccountVerified,
        boolean disputeOpen) {}
 public record DecisionResult(String decision,int score,Map<String,Object> metrics,List<String> actions) {}
}
