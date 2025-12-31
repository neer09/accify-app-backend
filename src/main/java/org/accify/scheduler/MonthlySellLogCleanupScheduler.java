package org.accify.scheduler;

import org.accify.repo.ETFSellNotificationLogRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonthlySellLogCleanupScheduler {

    private final ETFSellNotificationLogRepository logRepository;

    public MonthlySellLogCleanupScheduler(ETFSellNotificationLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    // Runs at 12:10 AM on the 1st day of every month
    @Scheduled(cron = "0 0 1 1 * ?")
    public void cleanupLogs() {
        logRepository.deleteAllLogs();
        //System.out.println("Deleted all ETF sell logs on the 1st of the month.");
    }
}
