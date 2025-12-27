package org.accify.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TradingBalanceResponse {
    private double availableCash;
    private double usedMargin;
    private double openingBalance;
}
