/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicashapplication.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
public class CashAllocationService {
    public AllocationResult allocate(@Valid AllocationRequest request) {
        Set<String> invoiceNumbers = new HashSet<>();
        for (InvoiceCandidate invoice : request.invoices()) {
            if (!invoiceNumbers.add(invoice.invoiceNo())) throw new IllegalArgumentException("发票号不能重复: " + invoice.invoiceNo());
        }
        List<InvoiceCandidate> eligible = request.invoices().stream()
            .filter(invoice -> invoice.customerCode().equals(request.customerCode()))
            .sorted(Comparator.comparing(InvoiceCandidate::referenceMatched).reversed()
                .thenComparing(InvoiceCandidate::dueDate).thenComparing(InvoiceCandidate::invoiceNo))
            .toList();
        BigDecimal remaining = request.receivedAmount();
        List<AllocationLine> lines = new ArrayList<>();
        int confidencePoints = 0;
        for (InvoiceCandidate invoice : eligible) {
            if (remaining.signum() <= 0) break;
            BigDecimal amount = remaining.min(invoice.openAmount()).setScale(2, RoundingMode.HALF_UP);
            if (amount.signum() == 0) continue;
            lines.add(new AllocationLine(invoice.invoiceNo(), amount, invoice.openAmount().subtract(amount),
                invoice.referenceMatched() ? "付款附言命中，按到期日优先" : "客户一致，按到期日补充分配"));
            remaining = remaining.subtract(amount);
            confidencePoints += invoice.referenceMatched() ? 100 : 72;
        }
        remaining = remaining.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        BigDecimal allocated = request.receivedAmount().subtract(remaining).setScale(2, RoundingMode.HALF_UP);
        String status = remaining.compareTo(request.tolerance()) <= 0 ? "FULLY_ALLOCATED" : lines.isEmpty() ? "UNAPPLIED" : "PARTIALLY_ALLOCATED";
        int confidence = lines.isEmpty() ? 0 : Math.min(100, confidencePoints / lines.size());
        List<String> warnings = new ArrayList<>();
        if (remaining.compareTo(request.tolerance()) > 0) warnings.add("仍有未分配金额 " + remaining + "，需进入待认领队列");
        long excluded = request.invoices().size() - eligible.size();
        if (excluded > 0) warnings.add(excluded + " 张非本客户发票已被隔离");
        return new AllocationResult(request.bankTransactionNo(), status, allocated, remaining, confidence, lines, warnings);
    }

    public record AllocationRequest(@NotBlank String bankTransactionNo, @NotBlank String customerCode,
                                    @NotNull @DecimalMin("0.01") BigDecimal receivedAmount,
                                    @NotNull @DecimalMin("0.00") BigDecimal tolerance,
                                    @NotEmpty List<@Valid InvoiceCandidate> invoices) {}
    public record InvoiceCandidate(@NotBlank String invoiceNo, @NotBlank String customerCode,
                                   @NotNull @DecimalMin("0.01") BigDecimal openAmount,
                                   @NotNull LocalDate dueDate, boolean referenceMatched) {}
    public record AllocationLine(String invoiceNo, BigDecimal allocatedAmount, BigDecimal remainingOpenAmount, String reason) {}
    public record AllocationResult(String bankTransactionNo, String status, BigDecimal allocatedAmount,
                                   BigDecimal unappliedAmount, int confidence, List<AllocationLine> allocations,
                                   List<String> warnings) {}
}
