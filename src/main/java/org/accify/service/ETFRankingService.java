package org.accify.service;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.LTPQuote;
import org.accify.cache.KiteInstrumentCache;
import org.accify.component.KiteClientWrapper;
import org.accify.dto.ETFRank;
import org.accify.entity.ETF;
import org.accify.repo.ETFRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ETFRankingService {

    private final ETFRepository etfRepository;
    private final KiteClientWrapper kiteClient;
    private final KiteAuthService kiteAuthService;

    private List<ETF> cachedEtfs = Collections.emptyList();
    private long lastEtfCacheUpdate = 0L;
    private static final long ETF_CACHE_REFRESH_MS = 24 * 60 * 60 * 1000; // 24 hours

    // Instrument Token Cache (load at startup)
    private final Map<String, Long> instrumentTokenMap;

    private static final Logger log = LoggerFactory.getLogger(ETFRankingService.class);

    public ETFRankingService(ETFRepository etfRepository, KiteClientWrapper kiteClient, KiteAuthService kiteAuthService) {
        this.etfRepository = etfRepository;
        this.kiteClient = kiteClient;
        this.kiteAuthService = kiteAuthService;
        this.instrumentTokenMap = loadInstrumentTokens();
    }

    public List<ETFRank> getTop15By20DMA() {
        List<ETFRank> result = new ArrayList<>();
        try {
            log.info("Fetching all ETFs!");
            List<ETF> etfs = getCachedEtfs(); // use cache
            log.info("ETF Map size : {}", etfs.size());

            List<String> symbols = etfs.stream()
                    .map(e -> e.getExchange() + ":" + e.getTradingSymbol())
                    .toList();

            // CMP
            log.info("Finding CMP for all ETFs!");
            Map<String, LTPQuote> ltpMap = kiteClient.getLtp(symbols);
            log.info("LTP Map size : {}", ltpMap.size());

            for (ETF etf : etfs) {

                String key = etf.getExchange() + ":" + etf.getTradingSymbol();
                Long token = instrumentTokenMap.get(key);

                if (token == null) continue;

                double dma20 = calculate20DMA(token);
                if (dma20 == 0) continue;

                double cmp = ltpMap.get(key).lastPrice;

                double diff = cmp - dma20;
                double pct = (diff / dma20) * 100;

                result.add(ETFRank.builder()
                        .symbol(etf.getTradingSymbol())
                        .cmp(cmp)
                        .dma20(dma20)
                        .diff(diff)
                        .percentChange(pct)
                        .build()
                );
            }
        } catch (Exception | KiteException e) {
            log.error("Failed to get Top 15 by 20 DMA", e);
        }

        return result.stream()
                .sorted(Comparator.comparingDouble(ETFRank::getPercentChange))
                .limit(15)
                .toList();
    }

    private double calculate20DMA(Long instrumentToken) {
        try {
            LocalDate to = LocalDate.now();
            LocalDate from = to.minusDays(35);

            HistoricalData data = kiteClient.getDailyCandles(
                    instrumentToken,
                    from + " 09:15:00",
                    to + " 15:30:00"
            );

            List<HistoricalData> candles = data.dataArrayList;

            if (candles.size() < 20) return 0;

            return candles.stream()
                    .skip(candles.size() - 20)
                    .mapToDouble(c -> c.close)
                    .average()
                    .orElse(0);

        } catch (Exception | KiteException e) {
            log.error("Failed to calculate 20 DMA for token", e);
            return 0;
        }
    }

    private Map<String, Long> loadInstrumentTokens() {
        return KiteInstrumentCache.getInstance(kiteAuthService.getAccessToken()).getInstrumentTokenMap();
    }

    private List<ETF> getCachedEtfs() {
        if (cachedEtfs.isEmpty() || System.currentTimeMillis() - lastEtfCacheUpdate > ETF_CACHE_REFRESH_MS) {
            cachedEtfs = etfRepository.findByEnabledTrue();
            lastEtfCacheUpdate = System.currentTimeMillis();
        }
        return cachedEtfs;
    }
}
