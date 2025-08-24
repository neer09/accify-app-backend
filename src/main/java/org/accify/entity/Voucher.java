package org.accify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.accify.enums.VoucherType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "voucher")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "voucher_type")
    private VoucherType voucherType;

    @Column(name = "debit_account_type")
    private String debitAccountType;

    @Column(name = "debit_amount")
    private BigDecimal debitAmount;

    @Column(name = "credit_account_type")
    private String creditAccountType;

    @Column(name = "credit_amount")
    private BigDecimal creditAmount;

    @Column(name = "remarks")
    private String remarks;
}
