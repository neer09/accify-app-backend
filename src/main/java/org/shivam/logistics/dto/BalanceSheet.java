package org.shivam.logistics.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class BalanceSheet {
    private Map<String, BigDecimal> assets;
    private Map<String, BigDecimal> liabilities;
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
}
