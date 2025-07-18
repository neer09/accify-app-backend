package org.shivam.logistics.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class LedgerSummary {
    private String accountName;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private List<VoucherEntry> entries;
}
