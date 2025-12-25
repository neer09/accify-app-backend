package org.accify.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ETFRank {
    private String symbol;      // NIFTYBEES
    private double cmp;
    private double dma20;
    private double diff;
    private double percentChange;
}
