package com.jsh.erp.service.depotHead;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class DepotHeadTaxRate {

    private static final Map<String, BigDecimal> FINANCIAL_TAX_RATE_MAP = buildFinancialTaxRateMap();

    private DepotHeadTaxRate() {
    }

    public static BigDecimal calculateFinancialTaxAmount(String billingRate, BigDecimal totalFinancialPrice) {
        BigDecimal taxRate = FINANCIAL_TAX_RATE_MAP.get(billingRate);
        if (taxRate == null || totalFinancialPrice == null) {
            return BigDecimal.ZERO;
        }
        return totalFinancialPrice.multiply(taxRate);
    }

    public static BigDecimal calculateFinancialTaxAmountScaleUp(String billingRate, BigDecimal totalFinancialPrice) {
        return calculateFinancialTaxAmount(billingRate, totalFinancialPrice).setScale(4, BigDecimal.ROUND_UP);
    }

    public static boolean isFinancialTaxBillingRate(String billingRate) {
        return "不开票".equals(billingRate) || FINANCIAL_TAX_RATE_MAP.containsKey(billingRate);
    }

    private static Map<String, BigDecimal> buildFinancialTaxRateMap() {
        Map<String, BigDecimal> taxRateMap = new HashMap<>();
        taxRateMap.put("增票11个点", new BigDecimal("0.11"));
        taxRateMap.put("普票2个点", new BigDecimal("0.02"));
        taxRateMap.put("普票3个点", new BigDecimal("0.03"));
        taxRateMap.put("普票4.5", new BigDecimal("0.045"));
        return Collections.unmodifiableMap(taxRateMap);
    }
}