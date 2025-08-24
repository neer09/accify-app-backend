package org.accify.service;

import org.accify.entity.Voucher;
import org.accify.repo.VoucherRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public Voucher saveVoucher(Voucher voucher) {
        // Business logic: debit == credit validation
        if (voucher.getDebitAmount() == null || voucher.getCreditAmount() == null) {
            throw new IllegalArgumentException("Amounts cannot be null");
        }

        if (voucher.getDebitAmount().compareTo(voucher.getCreditAmount()) != 0) {
            throw new IllegalArgumentException("Debit and credit amounts must be equal");
        }

        // Optional: normalize account names (capitalize, trim)
        voucher.setDebitAccountType(voucher.getDebitAccountType().trim().toUpperCase());
        voucher.setCreditAccountType(voucher.getCreditAccountType().trim().toUpperCase());

        return voucherRepository.save(voucher);
    }

    public Voucher updateVoucher(Long id, Voucher updated) {
        Voucher existing = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found with id: " + id));

        if (updated.getDebitAmount().compareTo(updated.getCreditAmount()) != 0) {
            throw new IllegalArgumentException("Debit and credit amounts must be equal");
        }

        existing.setDate(updated.getDate());
        existing.setVoucherType(updated.getVoucherType());
        existing.setDebitAccountType(updated.getDebitAccountType().trim().toUpperCase());
        existing.setDebitAmount(updated.getDebitAmount());
        existing.setCreditAccountType(updated.getCreditAccountType().trim().toUpperCase());
        existing.setCreditAmount(updated.getCreditAmount());
        existing.setRemarks(updated.getRemarks());

        return voucherRepository.save(existing);
    }

    public void deleteVoucher(Long id) {
        if (!voucherRepository.existsById(id)) {
            throw new RuntimeException("Voucher not found with id: " + id);
        }
        voucherRepository.deleteById(id);
    }

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }
}
