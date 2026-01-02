package org.accify.component;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AutoBuyDecisionSymbol {

    private final AtomicReference<String> buyDecision = new AtomicReference<>("NONE");

    public String get() {
        return buyDecision.get();
    }

    public void set(String value) {
        buyDecision.set(value);
    }
}
