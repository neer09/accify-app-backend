package org.accify.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ETFAutoSellSchedulerToggleService {

    private final AtomicBoolean enabled = new AtomicBoolean(false);
    private final ETFAutoSellService sellService;

    public ETFAutoSellSchedulerToggleService(ETFAutoSellService sellService) {
        this.sellService = sellService;
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void enable() {
        enabled.set(true);
        sellService.sellIfProfitAbove(3.0);
    }

    public void disable() {
        enabled.set(false);
    }
}
