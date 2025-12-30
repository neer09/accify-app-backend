package org.accify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.accify.component.KiteClientWrapper;
import org.accify.dto.TradingBalanceResponse;
import org.accify.dto.Update;
import org.accify.service.ETFAutoBuySchedulerToggleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/telegram")
@CrossOrigin
public class TelegramController {

    private static final Logger logger = LoggerFactory.getLogger(TelegramController.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private static final long ALLOWED_SENDER_ID = 6589970557L;
    private final ETFAutoBuySchedulerToggleService toggleService;
    private final KiteClientWrapper kiteClient;

    public TelegramController(ETFAutoBuySchedulerToggleService toggleService, KiteClientWrapper kiteClient) {
        this.toggleService = toggleService;
        this.kiteClient = kiteClient;
    }

    @PostMapping("/callback")
    public void onUpdateReceived(@RequestBody String rawJson) {
        logger.info("Raw Telegram update JSON: {}", rawJson);
        // Parse JSON to DTO
        try {
            Update update = mapper.readValue(rawJson, Update.class);

            if (update.message != null && update.message.text != null) {
                Long userId = update.message.from.id;
                String text = update.message.text;
                logger.info("Received message from {}: {}", userId, text);

                // Sender ID validation
                if (!userId.equals(ALLOWED_SENDER_ID)) {
                    logger.info("Message from unauthorized sender {} ignored", userId);
                    return; // stop processing
                }

                // Auto Buy validation
                if (!toggleService.isEnabled()) {
                    logger.info("ETF Auto-Buy is disabled!");
                    return; // etf auto buy disabled
                }

                // Amount check
                TradingBalanceResponse tradingBalance = kiteClient.getTradingBalance();
                double availableCash = tradingBalance.getAvailableCash() / 20;
                double injectAmount = availableCash / 2;

                // Extract symbol
                String symbol = extractSymbol(text);
                if (symbol == null) {
                    logger.info("Could not extract symbol from message: {}", text);
                    return;
                }
                logger.info("Extracted symbol from message: {}", symbol);

                // Place buy order
                logger.info("Placing buy order for telegram notification for: {}", symbol);
                kiteClient.placeBuyOrder(symbol, injectAmount);
            } else {
                logger.info("Received update without text message: {}", rawJson);
            }
        } catch (Exception | KiteException e) {
            logger.error("Failed to parse or process telegram notification", e);
        }
    }

    public String extractSymbol(String message) {
        if (message == null || message.isEmpty()) {
            return null;
        }

        // Find the position of "YouTube."
        int pos = message.indexOf("YouTube.");
        if (pos == -1) {
            return null; // "YouTube." not found
        }

        // Get the substring after "YouTube."
        String afterYouTube = message.substring(pos + "YouTube.".length()).trim();

        // Now split by space or dash to get the first token
        String[] parts = afterYouTube.split("\\s|-");
        if (parts.length > 0) {
            return parts[0].trim();
        }

        return null;
    }
}
