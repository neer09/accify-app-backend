package org.accify.service;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class KiteAuthService {

    private volatile String accessToken; // cached token

    public void generateAndStoreAccessToken(String requestToken) {
        try {
            KiteConnect kiteConnect = new KiteConnect("iwvet4wgrt7akw4i");
            kiteConnect.setUserId("RMY995");
            User user = kiteConnect.generateSession(requestToken, "6fgddahl9eysx8iq499coak93dbzkb6w");
            this.accessToken = user.accessToken;
        } catch (Exception | KiteException e) {
            // Failed to generate access token
        }
    }

    public boolean isTokenAvailable() {
        return accessToken != null;
    }
}
