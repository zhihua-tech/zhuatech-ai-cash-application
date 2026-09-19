/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicashapplication.controller;
import cn.zhuatech.aicashapplication.common.ApiResponse;
import cn.zhuatech.aicashapplication.service.DomainDecisionService;
import cn.zhuatech.aicashapplication.service.CashAllocationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/domain") public class DomainDecisionController {
 private final DomainDecisionService service; private final CashAllocationService cashAllocationService;
 public DomainDecisionController(DomainDecisionService service, CashAllocationService cashAllocationService){this.service=service;this.cashAllocationService=cashAllocationService;}
 @PostMapping("/decision") public ApiResponse<DomainDecisionService.DecisionResult> assess(@Valid @RequestBody DomainDecisionService.DecisionRequest request){return ApiResponse.ok(service.assess(request));}
 @PostMapping("/cash-allocation") public ApiResponse<CashAllocationService.AllocationResult> allocate(@Valid @RequestBody CashAllocationService.AllocationRequest request){return ApiResponse.ok(cashAllocationService.allocate(request));}
}
