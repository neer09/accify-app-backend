package org.accify.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class VoucherEntry {
    private LocalDate date;
    private String type;
    private BigDecimal debit;
    private BigDecimal credit;
    private String remarks;
}
