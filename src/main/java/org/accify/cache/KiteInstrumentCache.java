package org.accify.cache;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class KiteInstrumentCache {

    private static KiteInstrumentCache instance;

    private static final Logger log = LoggerFactory.getLogger(KiteInstrumentCache.class);

    // Cached map: "EXCHANGE:TRADINGSYMBOL" -> instrument_token
    @Getter
    private Map<String, Long> instrumentTokenMap = Collections.emptyMap();
    private long lastUpdated = 0L; // timestamp of last refresh

    private static final long CACHE_REFRESH_INTERVAL_MS = 24 * 60 * 60 * 1000; // 24 hours
    private static final String INSTRUMENTS_URL = "https://api.kite.trade/instruments";

    private KiteInstrumentCache(String accessToken) {
        refreshCache(accessToken);
    }

    public static synchronized KiteInstrumentCache getInstance(String accessToken) {
        if (instance == null) {
            instance = new KiteInstrumentCache(accessToken);
        } else if (System.currentTimeMillis() - instance.lastUpdated > CACHE_REFRESH_INTERVAL_MS) {
            instance.refreshCache(accessToken);
        }
        return instance;
    }

    private void refreshCache(String accessToken) {
        Map<String, Long> tokenMap = new HashMap<>();
        try {
            log.info("Fetching instrument cache!");
            URL url = new URL(INSTRUMENTS_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(60000);

            conn.setRequestProperty(
                    "Authorization",
                    "token " + "iwvet4wgrt7akw4i" + ":" + accessToken.trim()
            );
            conn.setRequestProperty("X-Kite-Version", "3");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                return; // keep old cache
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {

                String line;
                boolean firstLine = true;

                while ((line = br.readLine()) != null) {
                    if (firstLine) { // skip header
                        firstLine = false;
                        continue;
                    }

                    String[] parts = line.split(",", -1);
                    if (parts.length < 3) continue;

                    long instrumentToken = Long.parseLong(parts[0].trim());
                    String tradingSymbol = parts[2].trim();
                    String exchange = parts[11].trim(); // correct exchange column

                    tokenMap.put(exchange + ":" + tradingSymbol, instrumentToken);
                }
            }

            // Replace old cache atomically
            instrumentTokenMap = Collections.unmodifiableMap(tokenMap);
            lastUpdated = System.currentTimeMillis();

        } catch (Exception e) {
            log.error("Failed to refresh instrument cache, keeping old data", e);
        }
    }
}
