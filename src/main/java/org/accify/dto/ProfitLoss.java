package org.accify.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProfitLoss {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netProfitOrLoss;
}
