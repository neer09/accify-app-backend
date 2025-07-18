package org.shivam.logistics.controller;

import lombok.RequiredArgsConstructor;
import org.shivam.logistics.dto.BalanceSheet;
import org.shivam.logistics.dto.LedgerSummary;
import org.shivam.logistics.dto.ProfitLoss;
import org.shivam.logistics.dto.TrialBalance;
import org.shivam.logistics.service.LedgerService;
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

