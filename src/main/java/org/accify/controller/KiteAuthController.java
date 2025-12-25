package org.accify.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.accify.service.KiteAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class KiteAuthController {

    private final KiteAuthService kiteAuthService;

    private static final Logger log = LoggerFactory.getLogger(KiteAuthController.class);

    public KiteAuthController(KiteAuthService kiteAuthService) {
        this.kiteAuthService = kiteAuthService;
    }

    /**
     * STEP 1: Redirect user to Kite login
     */
    @GetMapping("/kite/login")
    public void redirectToKite(HttpServletResponse response) {
        try {
            String loginUrl = "https://kite.zerodha.com/connect/login?v=3&api_key=iwvet4wgrt7akw4i";
            log.info("Redirecting user to Kite login");
            response.sendRedirect(loginUrl);
        } catch (IOException e) {
            log.error("Failed to redirect to Kite login", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * STEP 2: Capture request_token after successful login
     */
    @GetMapping("/kite/callback")
    public String kiteCallback(@RequestParam("request_token") String requestToken) {
        log.info("Kite callback received. request_token={}", requestToken);
        kiteAuthService.generateAndStoreAccessToken(requestToken);
        return "Kite login successful. Access token generated.";
    }

    /**
     * STEP 3: Logout user from Kite
     */
    @GetMapping("/kite/logout")
    public void kiteLogout(HttpServletResponse response) {
        try {
            log.info("Clearing Kite access token and logging out");
            kiteAuthService.clearAccessToken();
            response.sendRedirect("https://kite.zerodha.com/connect/logout");
        } catch (IOException e) {
            log.error("Failed to redirect to Kite logout", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
