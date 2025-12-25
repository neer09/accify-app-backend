package org.accify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "etf_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ETF {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String exchange;        // NSE
    private String tradingSymbol;   // NIFTYBEES
    private boolean enabled;
}
