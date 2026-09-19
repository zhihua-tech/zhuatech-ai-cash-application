/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicashapplication.controller;
import cn.zhuatech.aicashapplication.common.ApiResponse;
import cn.zhuatech.aicashapplication.service.DomainDecisionService;
import cn.zhuatech.aicashapplication.service.CashAllocationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController @RequestMapping("/api/domain") public class DomainDecisionController {
 private final DomainDecisionService service; private final CashAllocationService cashAllocationService;
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public DomainDecisionController(DomainDecisionService service, CashAllocationService cashAllocationService){this.service=service;this.cashAllocationService=cashAllocationService;}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @PostMapping("/decision") public ApiResponse<DomainDecisionService.DecisionResult> assess(@Valid @RequestBody DomainDecisionService.DecisionRequest request){return ApiResponse.ok(service.assess(request));}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @PostMapping("/cash-allocation") public ApiResponse<CashAllocationService.AllocationResult> allocate(@Valid @RequestBody CashAllocationService.AllocationRequest request){return ApiResponse.ok(cashAllocationService.allocate(request));}
}
