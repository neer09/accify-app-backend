package org.accify.controller;

import org.accify.service.ETFAutoBuySchedulerToggleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduler/etf-auto-buy")
@CrossOrigin
public class ETFAutoBuySchedulerController {

    private final ETFAutoBuySchedulerToggleService toggleService;

    public ETFAutoBuySchedulerController(ETFAutoBuySchedulerToggleService toggleService) {
        this.toggleService = toggleService;
    }

    @PostMapping("/enable")
    public String enable() {
        toggleService.enable();
        return "ETF Auto Buy Scheduler ENABLED";
    }

    @PostMapping("/disable")
    public String disable() {
        toggleService.disable();
        return "ETF Auto Buy Scheduler DISABLED";
    }

    @GetMapping("/status")
    public boolean status() {
        return toggleService.isEnabled();
    }
}

