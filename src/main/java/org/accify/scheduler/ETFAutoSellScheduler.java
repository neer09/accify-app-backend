package org.accify.scheduler;

import org.accify.service.ETFAutoSellSchedulerToggleService;
import org.accify.service.ETFAutoSellService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ETFAutoSellScheduler {

    private final ETFAutoSellService sellService;
    private final ETFAutoSellSchedulerToggleService toggleService;

    @Value("${etf.auto-sell.profit-percent:3.0}")
    private double profitPercent;

    public ETFAutoSellScheduler(ETFAutoSellService sellService, ETFAutoSellSchedulerToggleService toggleService) {
        this.sellService = sellService;
        this.toggleService = toggleService;
    }

    @Scheduled(cron = "0 0 13,14,15 * * MON-FRI", zone = "Asia/Kolkata")
    public void run() {
        if (!toggleService.isEnabled()) {
            return; // scheduler disabled
        }

        try {
            sellService.sellIfProfitAbove(profitPercent);
        } catch (Exception e) {
            // Never crash scheduler
        }
    }
}
