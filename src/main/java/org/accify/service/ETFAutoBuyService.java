package org.accify.service;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Holding;
import com.zerodhatech.models.LTPQuote;
import org.accify.component.KiteClientWrapper;
import org.accify.dto.ETFRank;
import org.accify.dto.ETFStatus;
import org.accify.dto.TradingBalanceResponse;
import org.accify.entity.ETFCategoryMapping;
import org.accify.repo.ETFCategoryMappingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ETFAutoBuyService {

    private static final Logger log = LoggerFactory.getLogger(ETFAutoBuyService.class);

    private final ETFRankingService etfRankingService;

    private final ETFCategoryMappingRepository etfCategoryMappingRepository;

    private final KiteClientWrapper kiteClient;

    public ETFAutoBuyService(ETFRankingService etfRankingService, ETFCategoryMappingRepository etfCategoryMappingRepository, KiteClientWrapper kiteClient) {
        this.etfRankingService = etfRankingService;
        this.etfCategoryMappingRepository = etfCategoryMappingRepository;
        this.kiteClient = kiteClient;
    }
    public void buyETF() {
        try {
            List<ETFRank> etfs= etfRankingService.getTop15By20DMA();
            HashMap<String, ETFStatus> map = new HashMap<>();

            for (ETFRank etf : etfs) {
                if(etf.getSymbol().equals("MAFANG") || etf.getSymbol().equals("MAHKTECH")) {
                    continue;
                }
                ETFCategoryMapping etfCategoryMapping = etfCategoryMappingRepository.findBySymbol(etf.getSymbol());
                String category = "Miscellaneous";
                if(etfCategoryMapping != null) {
                    category = etfCategoryMapping.getCategory();
                }
                ETFStatus etfStatus = new ETFStatus(etf.getSymbol(), etf.getPercentChange());
                if(map.containsKey(category)) {
                    ETFStatus currentETFStatus = map.get(category);
                    if(currentETFStatus.getPercentChange() > etf.getPercentChange()) {
                        map.put(category, etfStatus);
                    }
                } else {
                    map.put(category, etfStatus);
                }
            }

            Map<String, ETFStatus> sortedMap =
                    map.entrySet()
                            .stream()
                            .sorted(Comparator.comparingDouble(e -> e.getValue().getPercentChange()))
                            .collect(
                                    LinkedHashMap::new,
                                    (m, e) -> m.put(e.getKey(), e.getValue()),
                                    LinkedHashMap::putAll
                            );

            //Fetch all holdings
            List<Holding> holdings = kiteClient.getHoldings();

            Map<String, Holding> holdingMap = new HashMap<>();
            for (Holding holding : holdings) {
                holdingMap.put(holding.tradingSymbol, holding);
            }

            int count = 1;
            String buySymbol = "";
            double maxDiff = Double.NEGATIVE_INFINITY;
            TradingBalanceResponse tradingBalance = kiteClient.getTradingBalance();
            double availableCash = tradingBalance.getAvailableCash() / 20;
            double injectAmount = availableCash / 2;

            for (Map.Entry<String, ETFStatus> entry : sortedMap.entrySet()) {
                if(count > 3) {
                    break;
                }
                ETFStatus etf = entry.getValue();
                String symbol = etf.getSymbol();
                if (!holdingMap.containsKey(symbol)) {
                    kiteClient.placeBuyOrder(symbol, injectAmount);
                    return;
                } else {
                    Holding holding = holdingMap.get(symbol);
                    double avgPrice = holding.averagePrice;
                    String instrument = "NSE:" + symbol;
                    List<String> instrumentList = new ArrayList<>();
                    instrumentList.add(instrument);
                    Map<String, LTPQuote> ltpMap = kiteClient.getLtp(instrumentList);
                    double ltp = ltpMap.get(instrument).lastPrice;
                    double diff = Math.abs(ltp - avgPrice);
                    if (diff > maxDiff) {
                        maxDiff = diff;
                        buySymbol = symbol;
                    }
                }
                count++;
            }

            kiteClient.placeBuyOrder(buySymbol, injectAmount);
        } catch(Exception | KiteException e) {
            log.error("Failed to place an auto-buy order", e);
        }
    }
}
