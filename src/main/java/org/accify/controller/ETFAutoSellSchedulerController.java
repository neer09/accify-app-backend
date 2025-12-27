package org.accify.controller;

import org.accify.service.ETFAutoSellSchedulerToggleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduler/etf-auto-sell")
@CrossOrigin
public class ETFAutoSellSchedulerController {

    private final ETFAutoSellSchedulerToggleService toggleService;

    public ETFAutoSellSchedulerController(ETFAutoSellSchedulerToggleService toggleService) {
        this.toggleService = toggleService;
    }

    @PostMapping("/enable")
    public String enable() {
        toggleService.enable();
        return "ETF Auto Sell Scheduler ENABLED";
    }

    @PostMapping("/disable")
    public String disable() {
        toggleService.disable();
        return "ETF Auto Sell Scheduler DISABLED";
    }

    @GetMapping("/status")
    public boolean status() {
        return toggleService.isEnabled();
    }
}

