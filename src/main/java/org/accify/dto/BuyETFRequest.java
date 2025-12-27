package org.accify.dto;

import lombok.Data;

@Data
public class BuyETFRequest {
    private String tradingSymbol; // e.g. NIFTYBEES
    private double amount;        // amount in INR
}
