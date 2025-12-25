package org.accify.cache;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class KiteInstrumentCache {

    private static KiteInstrumentCache instance;

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
            URL url = new URL(INSTRUMENTS_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "token " + "iwvet4wgrt7akw4i" + ":" + accessToken);
            conn.setRequestProperty("X-Kite-Version", "3");

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(new GZIPInputStream(conn.getInputStream())))) {

                String line;
                boolean firstLine = true;

                while ((line = br.readLine()) != null) {
                    if (firstLine) { // skip header
                        firstLine = false;
                        continue;
                    }

                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;

                    String tradingSymbol = parts[2].trim();
                    long instrumentToken = Long.parseLong(parts[0].trim());
                    String exchange = parts[1].trim();

                    tokenMap.put(exchange + ":" + tradingSymbol, instrumentToken);
                }
            }

            // Replace old cache atomically
            instrumentTokenMap = Collections.unmodifiableMap(tokenMap);
            lastUpdated = System.currentTimeMillis();
        } catch (Exception e) {
            // Failed to refresh instrument cache, keeping old data
        }
    }
}
