package org.accify.service;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.LTPQuote;
import org.accify.cache.KiteInstrumentCache;
import org.accify.component.KiteClientWrapper;
import org.accify.dto.ETFRank;
import org.accify.entity.ETF;
import org.accify.repo.ETFRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ETFRankingService {

    private final ETFRepository etfRepository;
    private final KiteClientWrapper kiteClient;

    private List<ETF> cachedEtfs = Collections.emptyList();
    private long lastEtfCacheUpdate = 0L;
    private static final long ETF_CACHE_REFRESH_MS = 24 * 60 * 60 * 1000; // 24 hours

    // Instrument Token Cache (load at startup)
    private final Map<String, Long> instrumentTokenMap;

    public ETFRankingService(ETFRepository etfRepository, KiteClientWrapper kiteClient) {
        this.etfRepository = etfRepository;
        this.kiteClient = kiteClient;
        this.instrumentTokenMap = loadInstrumentTokens();
    }

    public List<ETFRank> getTop10By20DMA() {
        List<ETFRank> result = new ArrayList<>();
        try {
            List<ETF> etfs = getCachedEtfs(); // use cache

            List<String> symbols = etfs.stream()
                    .map(e -> e.getExchange() + ":" + e.getTradingSymbol())
                    .toList();

            // CMP
            Map<String, LTPQuote> ltpMap = kiteClient.getLtp(symbols);

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
            // Failed to get Top 10 by 20 DMA
        }

        return result.stream()
                .sorted(Comparator.comparingDouble(ETFRank::getPercentChange))
                .limit(10)
                .toList();
    }

    private double calculate20DMA(Long instrumentToken) {
        try {
            LocalDate to = LocalDate.now();
            LocalDate from = to.minusDays(30);

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
            // Failed to calculate 20 DMA for token
            return 0;
        }
    }

    private Map<String, Long> loadInstrumentTokens() {
        return KiteInstrumentCache.getInstance().getInstrumentTokenMap();
    }

    private List<ETF> getCachedEtfs() {
        if (cachedEtfs.isEmpty() || System.currentTimeMillis() - lastEtfCacheUpdate > ETF_CACHE_REFRESH_MS) {
            cachedEtfs = etfRepository.findByEnabledTrue();
            lastEtfCacheUpdate = System.currentTimeMillis();
        }
        return cachedEtfs;
    }
}
