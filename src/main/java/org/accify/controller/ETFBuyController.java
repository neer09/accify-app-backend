package org.accify.controller;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.accify.component.KiteClientWrapper;
import org.accify.dto.BuyETFRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/etf")
@CrossOrigin
public class ETFBuyController {

    private final KiteClientWrapper kiteClient;

    private static final Logger log = LoggerFactory.getLogger(ETFBuyController.class);

    public ETFBuyController(KiteClientWrapper kiteClient) {
        this.kiteClient = kiteClient;
    }

    @PostMapping("/buy")
    public void buyETF(@RequestBody BuyETFRequest request) {
        try {
            log.info("Placing Buy ETF Order!");
            kiteClient.placeBuyOrder(request.getTradingSymbol(), request.getAmount());
        } catch (Exception | KiteException e) {
            log.error("Failed to place Buy Order!", e);
        }
    }
}
