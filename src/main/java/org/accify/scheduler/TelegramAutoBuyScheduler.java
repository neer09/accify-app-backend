package org.accify.scheduler;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.accify.component.AutoBuyDecisionSymbol;
import org.accify.component.KiteClientWrapper;
import org.accify.dto.TradingBalanceResponse;
import org.accify.service.ETFAutoBuySchedulerToggleService;
import org.accify.service.TelegramNotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TelegramAutoBuyScheduler {

    private final ETFAutoBuySchedulerToggleService autoBuyToggle;
    private final TelegramNotificationService telegramAutoBuyToggle;
    private final KiteClientWrapper kiteClient;
    private final AutoBuyDecisionSymbol autoBuyDecisionSymbol;

    public TelegramAutoBuyScheduler(KiteClientWrapper kiteClient, ETFAutoBuySchedulerToggleService autoBuyToggle, TelegramNotificationService telegramAutoBuyToggle, AutoBuyDecisionSymbol autoBuyDecisionSymbol) {
        this.kiteClient = kiteClient;
        this.autoBuyToggle = autoBuyToggle;
        this.telegramAutoBuyToggle = telegramAutoBuyToggle;
        this.autoBuyDecisionSymbol = autoBuyDecisionSymbol;
    }

    @Scheduled(cron = "0 10 15 * * MON-FRI", zone = "Asia/Kolkata")
    public void run() {
        if (!autoBuyToggle.isEnabled() || !telegramAutoBuyToggle.isEnabled()) {
            return; // scheduler disabled
        }

        try {
            // Calculate inject amount
            TradingBalanceResponse tradingBalance = kiteClient.getTradingBalance();
            double availableCash = tradingBalance.getAvailableCash() / 20;
            double injectAmount = availableCash / 2;

            // Fetch buy symbol
            String buySymbol = autoBuyDecisionSymbol.get();

            // Place buy order
            kiteClient.placeBuyOrder(buySymbol, injectAmount);
        } catch (Exception | KiteException e) {
            // Never crash scheduler
        }
    }
}
