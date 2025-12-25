package org.accify.component;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.*;
import org.accify.service.KiteAuthService;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class KiteClientWrapper {

    private final KiteConnect kiteConnect;
    private final KiteAuthService kiteAuthService;

    public KiteClientWrapper(KiteAuthService kiteAuthService) {
        this.kiteAuthService = kiteAuthService;
        this.kiteConnect = new KiteConnect("iwvet4wgrt7akw4i");
    }

    private void ensureAccessToken() {
        kiteConnect.setAccessToken(kiteAuthService.getAccessToken());
    }

    public Map<String, LTPQuote> getLtp(List<String> symbols) throws Exception, KiteException {
        ensureAccessToken();
        String[] symbolArray = symbols.toArray(new String[0]);
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
}
