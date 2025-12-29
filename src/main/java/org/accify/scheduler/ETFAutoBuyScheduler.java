package org.accify.scheduler;

import org.accify.service.ETFAutoBuySchedulerToggleService;
import org.accify.service.ETFAutoBuyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ETFAutoBuyScheduler {

    private final ETFAutoBuyService buyService;
    private final ETFAutoBuySchedulerToggleService toggleService;

    public ETFAutoBuyScheduler(ETFAutoBuyService sellService, ETFAutoBuySchedulerToggleService toggleService) {
        this.buyService = sellService;
        this.toggleService = toggleService;
    }

    @Scheduled(cron = "0 0 15 * * MON-FRI", zone = "Asia/Kolkata")
    public void run() {
        if (!toggleService.isEnabled()) {
            return; // scheduler disabled
        }

        try {
            buyService.buyETF();
        } catch (Exception e) {
            // Never crash scheduler
        }
    }
}
