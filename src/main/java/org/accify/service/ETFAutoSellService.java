package org.accify.service;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Holding;
import org.accify.component.KiteClientWrapper;
import org.accify.entity.ETFWatchlist;
import org.accify.repo.ETFWatchlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ETFAutoSellService {

    private static final Logger log = LoggerFactory.getLogger(ETFAutoSellService.class);

    private final KiteClientWrapper kiteClient;

    private final ETFWatchlistRepository watchlistRepo;

    public ETFAutoSellService(KiteClientWrapper kiteClient, ETFWatchlistRepository watchlistRepo) {
        this.kiteClient = kiteClient;
        this.watchlistRepo = watchlistRepo;
    }

    public void sellIfProfitAbove(double percent) {
        try {
            //Fetch all holdings
            log.info("Fetch all holdings for Auto-Sell");
            List<Holding> holdings = kiteClient.getHoldings();

            // Fetch all watchlist symbols from DB
            Set<String> excludedETFs = watchlistRepo.findAll().stream()
                    .map(ETFWatchlist::getSymbol)
                    .collect(Collectors.toSet());

            for (Holding h : holdings) {
                if (!"CNC".equals(h.product)) continue;

                // Skip if ETF is in watchlist
                if (excludedETFs.contains(h.tradingSymbol)) {
                    log.info("Skipping {} as it is in watchlist", h.tradingSymbol);
                    continue;
                }

                double profitPct = ((h.lastPrice - h.averagePrice) / h.averagePrice) * 100;
                if (profitPct >= percent && h.quantity > 0) {
                    log.info("Auto-selling {} profit={}% threshold={}%", h.tradingSymbol, profitPct, percent);
                    kiteClient.placeSellOrder(h.tradingSymbol, h.quantity, h.averagePrice);
                }
            }
        } catch(Exception | KiteException e) {
            log.error("Failed to fetch holdings or place an auto-sell order", e);
        }
    }
}
