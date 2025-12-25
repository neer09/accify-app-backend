package org.accify.service;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Getter
@Service
public class KiteAuthService {

    private volatile String accessToken; // cached token

    private static final Logger log = LoggerFactory.getLogger(KiteAuthService.class);

    public void generateAndStoreAccessToken(String requestToken) {
        try {
            KiteConnect kiteConnect = new KiteConnect("iwvet4wgrt7akw4i");
            kiteConnect.setUserId("RMY995");
            User user = kiteConnect.generateSession(requestToken, "6fgddahl9eysx8iq499coak93dbzkb6w");
            log.info("Access Token received : {}", user.accessToken);
            this.accessToken = user.accessToken;
        } catch (Exception | KiteException e) {
            log.error("Failed to generate access token", e);
        }
    }

    public boolean isTokenAvailable() {
        return accessToken != null;
    }

    public void clearAccessToken() {
        this.accessToken = null;
        log.info("Access Token cleared!");
    }
}
