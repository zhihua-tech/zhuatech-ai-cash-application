/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicashapplication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@SpringBootTest @AutoConfigureMockMvc class CashAllocationApiTests {
 @Autowired MockMvc mvc;
 private static final String BODY="""
  {"bankTransactionNo":"BANK-2001","customerCode":"C-01","receivedAmount":1500,"tolerance":1,
   "invoices":[{"invoiceNo":"INV-02","customerCode":"C-01","openAmount":1000,"dueDate":"2026-10-01","referenceMatched":false},
               {"invoiceNo":"INV-01","customerCode":"C-01","openAmount":800,"dueDate":"2026-09-20","referenceMatched":true}]}
  """;
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void allocatesAcrossInvoicesWithExplainablePriority() throws Exception {
  mvc.perform(post("/api/domain/cash-allocation").with(httpBasic("operator","operator123")).contentType(MediaType.APPLICATION_JSON).content(BODY))
   .andExpect(status().isOk()).andExpect(jsonPath("$.data.status").value("FULLY_ALLOCATED"))
   .andExpect(jsonPath("$.data.allocatedAmount").value(1500)).andExpect(jsonPath("$.data.allocations.length()").value(2))
   .andExpect(jsonPath("$.data.allocations[0].invoiceNo").value("INV-01"));
 }
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void rejectsDuplicateInvoiceNumbers() throws Exception {
  String duplicate=BODY.replace("INV-02","INV-01");
  mvc.perform(post("/api/domain/cash-allocation").with(httpBasic("operator","operator123")).contentType(MediaType.APPLICATION_JSON).content(duplicate))
   .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("发票号不能重复: INV-01"));
 }
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void allocationRequiresAuthentication() throws Exception {mvc.perform(post("/api/domain/cash-allocation").contentType(MediaType.APPLICATION_JSON).content(BODY)).andExpect(status().isUnauthorized());}
}
