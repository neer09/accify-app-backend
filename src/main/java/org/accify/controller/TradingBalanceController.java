package org.accify.controller;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.accify.component.KiteClientWrapper;
import org.accify.dto.TradingBalanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@CrossOrigin
public class TradingBalanceController {

    private final KiteClientWrapper kiteClient;

    private static final Logger log = LoggerFactory.getLogger(TradingBalanceController.class);

    public TradingBalanceController(KiteClientWrapper kiteClient) {
        this.kiteClient = kiteClient;
    }

    @GetMapping("/balance")
    public TradingBalanceResponse getBalance() {
        TradingBalanceResponse response = null;
        try {
            log.info("Getting Trading Balance!");
            response = kiteClient.getTradingBalance();
        } catch (Exception | KiteException e) {
            log.error("Failed to fetch trading balance!", e);
        }
        return response;
    }
}
