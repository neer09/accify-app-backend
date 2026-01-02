package org.accify.service;

import org.accify.event.AutoSellEnabledEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ETFAutoSellSchedulerToggleService {

    private final AtomicBoolean enabled = new AtomicBoolean(false);
    private final ApplicationEventPublisher eventPublisher;

    @Value("${etf.auto-sell.profit-percent:3.0}")
    private double profitPercent;

    public ETFAutoSellSchedulerToggleService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void enable() {
        enabled.set(true);

        // Fire auto sell event immediately
        eventPublisher.publishEvent(new AutoSellEnabledEvent(profitPercent));
    }

    public void disable() {
        enabled.set(false);
    }
}
