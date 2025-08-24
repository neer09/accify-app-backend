package org.accify.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class TrialBalance {
    private Map<String, BigDecimal> totalDebits;
    private Map<String, BigDecimal> totalCredits;
    private BigDecimal grandTotalDebit;
    private BigDecimal grandTotalCredit;
}