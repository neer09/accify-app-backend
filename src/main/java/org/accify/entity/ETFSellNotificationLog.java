package org.accify.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "etf_sell_notification_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ETFSellNotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String symbol;

    @Column(nullable = false)
    private double ltp;

    @Column(name = "avg_price", nullable = false)
    private double avgPrice;

    @Column(nullable = false)
    private double gain;

    @Column(name = "gain_percent", nullable = false)
    private double gainPercent;

    @Column(name = "sell_timeframe", nullable = false)
    private LocalDateTime sellTimeframe;
}
