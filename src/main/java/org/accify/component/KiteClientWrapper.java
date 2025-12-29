package org.accify.component;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.*;
import org.accify.dto.TradingBalanceResponse;
import org.accify.service.KiteAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class KiteClientWrapper {

    private final KiteConnect kiteConnect;
    private final KiteAuthService kiteAuthService;

    private static final Logger log = LoggerFactory.getLogger(KiteClientWrapper.class);

    public KiteClientWrapper(KiteAuthService kiteAuthService) {
        this.kiteAuthService = kiteAuthService;
        this.kiteConnect = new KiteConnect("iwvet4wgrt7akw4i");
    }

    public Map<String, LTPQuote> getLtp(List<String> symbols) throws Exception, KiteException {
        ensureAccessToken();
        String[] symbolArray = symbols.toArray(new String[0]);
        log.info("Getting LTP");
        return kiteConnect.getLTP(symbolArray);
    }

    public HistoricalData getDailyCandles(Long instrumentToken, String from, String to) throws Exception, KiteException {
        ensureAccessToken();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // IST

        Date fromDate = sdf.parse(from);
        Date toDate = sdf.parse(to);

        return kiteConnect.getHistoricalData(
                fromDate,
                toDate,
                instrumentToken.toString(),
                "day",
                false,
                false
        );
    }

    public List<Holding> getHoldings() throws Exception, KiteException {
        ensureAccessToken();
        return kiteConnect.getHoldings();
    }

    public void placeSellOrder(String symbol, int qty) throws Exception, KiteException {
        ensureAccessToken();

        //Build order params
        OrderParams params = new OrderParams();
        params.tradingsymbol = symbol;
        params.exchange = "NSE";
        params.transactionType = "SELL";
        params.orderType = "MARKET";
        params.product = "CNC";
        params.validity = "DAY";
        params.quantity = qty;

        //Place order
        Order order = kiteConnect.placeOrder(params, "regular");
        log.info("ETF SELL placed | Symbol={} | Qty={} | OrderId={}", symbol, qty, order.orderId);
    }

    public void placeBuyOrder(String tradingSymbol, double amount) throws KiteException, Exception {
        ensureAccessToken();

        //Initial amount check
        if (amount <= 0) {
            log.info("Amount is zero. No buy allowed!");
            return;
        }

        //Fetch LTP
        String instrument = "NSE:" + tradingSymbol;
        Map<String, LTPQuote> ltpMap = kiteConnect.getLTP(new String[]{instrument});
        double ltp = ltpMap.get(instrument).lastPrice;

        //Calculate quantity
        int quantity = (int) (amount / ltp);
        if (quantity <= 0) {
            log.info("Amount too low to buy 1 unit. LTP={}", ltp);
            return;
        }

        //Build order params
        OrderParams params = new OrderParams();
        params.exchange = "NSE";
        params.tradingsymbol = tradingSymbol;
        params.transactionType = "BUY";
        params.quantity = quantity;
        params.orderType = "MARKET";
        params.product = "CNC"; // ETFs should be CNC
        params.validity = "DAY";

        //Place order
        Order order = kiteConnect.placeOrder(params, "regular");
        log.info("ETF BUY placed | Symbol={} | Qty={} | Amount={} | OrderId={}", tradingSymbol, quantity, amount, order.orderId);
    }

    public TradingBalanceResponse getTradingBalance() throws Exception, KiteException {
        ensureAccessToken();
        Margin margin = kiteConnect.getMargins("equity");
        return TradingBalanceResponse.builder()
                .availableCash(Double.parseDouble(margin.available.liveBalance))
                .usedMargin(Double.parseDouble(margin.utilised.debits))
                .openingBalance(Double.parseDouble(margin.available.cash))
                .build();
    }

    private void ensureAccessToken() {
        kiteConnect.setAccessToken(kiteAuthService.getAccessToken());
    }
}
