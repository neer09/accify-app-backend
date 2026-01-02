package org.accify.component;

import org.accify.event.AutoSellEnabledEvent;
import org.accify.service.ETFAutoSellService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AutoSellEnabledListener {

    private final ETFAutoSellService sellService;

    public AutoSellEnabledListener(ETFAutoSellService sellService) {
        this.sellService = sellService;
    }

    @EventListener
    public void onAutoSellEnabled(AutoSellEnabledEvent event) {
        sellService.sellIfProfitAbove(event.profitPercent());
    }
}
