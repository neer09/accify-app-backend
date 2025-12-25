package org.accify.controller;

import org.accify.service.KiteAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KiteAuthController {

    private final KiteAuthService kiteAuthService;

    public KiteAuthController(KiteAuthService kiteAuthService) {
        this.kiteAuthService = kiteAuthService;
    }

    /**
     * STEP 1: Redirect user to Zerodha login
     */
    @GetMapping("/kite/login")
    public String redirectToKite() {
        return "redirect:https://kite.zerodha.com/connect/login?v=3&api_key=iwvet4wgrt7akw4i";
    }

    /**
     * STEP 2: Capture request_token after login
     */
    @GetMapping("/kite/callback")
    public String kiteCallback(@RequestParam("request_token") String requestToken) {
        kiteAuthService.generateAndStoreAccessToken(requestToken);
        return "Kite login successful. Access token generated.";
    }

    /**
     * STEP 3: Redirect user to Zerodha logout
     */
    @GetMapping("/kite/logout")
    public String kiteLogout() {
        kiteAuthService.clearAccessToken(); // delete from cache
        return "redirect:https://kite.zerodha.com/connect/logout";
    }
}
