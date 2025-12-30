package org.accify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ETFStatus {
    private String symbol;
    private double percentChange;
}
