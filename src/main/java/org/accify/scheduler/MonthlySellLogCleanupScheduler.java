package org.accify.scheduler;

import org.accify.repo.ETFSellNotificationLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonthlySellLogCleanupScheduler {

    private final ETFSellNotificationLogRepository logRepository;
    private static final Logger log = LoggerFactory.getLogger(MonthlySellLogCleanupScheduler.class);

    public MonthlySellLogCleanupScheduler(ETFSellNotificationLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    // Runs at 1:30 AM on the 1st day of every month
    @Scheduled(cron = "0 30 1 1 * ?")
    public void cleanupLogs() {
        log.info("Deleting all ETF sell logs on the 1st of the month.");
        logRepository.deleteAllLogs();
        log.info("Deleted all ETF sell logs on the 1st of the month.");
    }
}
