package org.accify.controller;

import org.accify.entity.Voucher;
import org.accify.service.VoucherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@CrossOrigin
public class VoucherController {
    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping
    public Voucher send(@RequestBody Voucher voucher) {
        return voucherService.saveVoucher(voucher);
    }

    @PutMapping("/{id}")
    public Voucher update(@PathVariable Long id, @RequestBody Voucher updatedVoucher) {
        return voucherService.updateVoucher(id, updatedVoucher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Voucher> getAll() {
        return voucherService.getAllVouchers();
    }
}

