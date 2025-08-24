package org.accify.service;

import org.accify.dto.*;
import org.accify.entity.Voucher;
import org.accify.repo.VoucherRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LedgerService {

    private final VoucherRepository voucherRepository;

    public LedgerService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public LedgerSummary getLedgerSummary(String accountName) {
        List<Voucher> vouchers = voucherRepository.findByDebitOrCredit(accountName);

        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        List<VoucherEntry> entries = new ArrayList<>();

        for (Voucher v : vouchers) {
            BigDecimal debit = v.getDebitAccountType().equals(accountName) ? v.getDebitAmount() : BigDecimal.ZERO;
            BigDecimal credit = v.getCreditAccountType().equals(accountName) ? v.getCreditAmount() : BigDecimal.ZERO;

            totalDebit = totalDebit.add(debit);
            totalCredit = totalCredit.add(credit);

            entries.add(VoucherEntry.builder()
                    .date(v.getDate())
                    .type(v.getVoucherType().name())
                    .debit(debit)
                    .credit(credit)
                    .remarks(v.getRemarks())
                    .build());
        }

        return LedgerSummary.builder()
                .accountName(accountName)
                .totalDebit(totalDebit)
                .totalCredit(totalCredit)
                .entries(entries)
                .build();
    }

    public TrialBalance getTrialBalance() {
        List<Voucher> vouchers = voucherRepository.findAll();

        Map<String, BigDecimal> debitMap = new HashMap<>();
        Map<String, BigDecimal> creditMap = new HashMap<>();

        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (Voucher v : vouchers) {
            debitMap.merge(v.getDebitAccountType(), v.getDebitAmount(), BigDecimal::add);
            creditMap.merge(v.getCreditAccountType(), v.getCreditAmount(), BigDecimal::add);
            totalDebit = totalDebit.add(v.getDebitAmount());
            totalCredit = totalCredit.add(v.getCreditAmount());
        }

        return TrialBalance.builder()
                .totalDebits(debitMap)
                .totalCredits(creditMap)
                .grandTotalDebit(totalDebit)
                .grandTotalCredit(totalCredit)
                .build();

    }

    public ProfitLoss getProfitLoss() {
        List<Voucher> vouchers = voucherRepository.findAll();

        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;

        List<String> incomeAccounts = List.of("SALES", "OUTPUT GST");
        List<String> expenseAccounts = List.of("PURCHASE", "RENT", "SALARY", "OFFICE EXPENSES", "UTILITIES");

        for (Voucher v : vouchers) {
            if (incomeAccounts.contains(v.getCreditAccountType())) {
                income = income.add(v.getCreditAmount());
            }
            if (expenseAccounts.contains(v.getDebitAccountType())) {
                expense = expense.add(v.getDebitAmount());
            }
        }

        return ProfitLoss.builder()
                .totalIncome(income)
                .totalExpenses(expense)
                .netProfitOrLoss(income.subtract(expense))
                .build();
    }

    public BalanceSheet getBalanceSheet() {
        List<Voucher> vouchers = voucherRepository.findAll();

        Map<String, BigDecimal> assets = new HashMap<>();
        Map<String, BigDecimal> liabilities = new HashMap<>();

        List<String> assetAccounts = List.of("BANK", "CASH");
        List<String> liabilityAccounts = List.of("CAPITAL", "LOANS", "CREDITORS");

        for (Voucher v : vouchers) {
            // Assets
            if (assetAccounts.contains(v.getDebitAccountType())) {
                assets.merge(v.getDebitAccountType(), v.getDebitAmount(), BigDecimal::add);
            }
            if (assetAccounts.contains(v.getCreditAccountType())) {
                assets.merge(v.getCreditAccountType(), v.getCreditAmount().negate(), BigDecimal::add);
            }

            // Liabilities
            if (liabilityAccounts.contains(v.getCreditAccountType())) {
                liabilities.merge(v.getCreditAccountType(), v.getCreditAmount(), BigDecimal::add);
            }
            if (liabilityAccounts.contains(v.getDebitAccountType())) {
                liabilities.merge(v.getDebitAccountType(), v.getDebitAmount().negate(), BigDecimal::add);
            }
        }

        BigDecimal totalAssets = assets.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalLiabilities = liabilities.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        return BalanceSheet.builder()
                .assets(assets)
                .liabilities(liabilities)
                .totalAssets(totalAssets)
                .totalLiabilities(totalLiabilities)
                .build();
    }
}
