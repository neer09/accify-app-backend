package org.accify.service;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import lombok.Getter;
import org.accify.cache.KiteInstrumentCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Getter
@Service
public class KiteAuthService {

    private volatile String accessToken; // cached token

    private final ETFAutoBuySchedulerToggleService buyToggleService;
    private final ETFAutoSellSchedulerToggleService sellToggleService;
    private final TelegramNotificationService telegramNotificationService;

    private static final Logger log = LoggerFactory.getLogger(KiteAuthService.class);

    public KiteAuthService(ETFAutoBuySchedulerToggleService buyToggleService, ETFAutoSellSchedulerToggleService sellToggleService, TelegramNotificationService telegramNotificationService) {
        this.buyToggleService = buyToggleService;
        this.sellToggleService = sellToggleService;
        this.telegramNotificationService = telegramNotificationService;
    }

    public void generateAndStoreAccessToken(String requestToken) {
        try {
            KiteConnect kiteConnect = new KiteConnect("iwvet4wgrt7akw4i");
            kiteConnect.setUserId("RMY995");
            User user = kiteConnect.generateSession(requestToken, "6fgddahl9eysx8iq499coak93dbzkb6w");
            log.info("Access Token received : {}", user.accessToken);
            this.accessToken = user.accessToken;

            KiteInstrumentCache.getInstance(this.accessToken);
            log.info("KiteInstrumentCache initialized after login.");

            buyToggleService.enable();
            sellToggleService.enable();
            log.info("Enabled Auto-Buy & Auto-Sell!");

            telegramNotificationService.disable();
            log.info("Disabled Telegram Notification!");
        } catch (Exception | KiteException e) {
            log.error("Failed to generate access token", e);
        }
    }

    public boolean isTokenAvailable() {
        return accessToken != null;
    }

    public void clearAccessToken() {
        this.accessToken = null;
        log.info("Access Token cleared!");
    }
}
