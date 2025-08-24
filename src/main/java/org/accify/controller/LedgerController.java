package org.accify.controller;

import lombok.RequiredArgsConstructor;
import org.accify.dto.BalanceSheet;
import org.accify.dto.LedgerSummary;
import org.accify.dto.ProfitLoss;
import org.accify.dto.TrialBalance;
import org.accify.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class LedgerController {

    private final LedgerService ledgerService;

    @GetMapping("/ledgers/{accountName}")
    public ResponseEntity<LedgerSummary> getLedgerSummary(@PathVariable String accountName) {
        return ResponseEntity.ok(ledgerService.getLedgerSummary(accountName));
    }

    @GetMapping("/reports/trial-balance")
    public ResponseEntity<TrialBalance> getTrialBalance() {
        return ResponseEntity.ok(ledgerService.getTrialBalance());
    }

    @GetMapping("/reports/profit-loss")
    public ResponseEntity<ProfitLoss> getProfitLoss() {
        return ResponseEntity.ok(ledgerService.getProfitLoss());
    }

    @GetMapping("/reports/balance-sheet")
    public ResponseEntity<BalanceSheet> getBalanceSheet() {
        return ResponseEntity.ok(ledgerService.getBalanceSheet());
    }
}

