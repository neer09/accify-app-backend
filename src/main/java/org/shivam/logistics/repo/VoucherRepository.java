package org.shivam.logistics.repo;

import org.shivam.logistics.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    List<Voucher> findByDebitAccountTypeIgnoreCaseOrCreditAccountTypeIgnoreCase(String debit, String credit);

    @Query("SELECT v FROM Voucher v WHERE v.debitAccountType = :account OR v.creditAccountType = :account")
    List<Voucher> findByDebitOrCredit(@Param("account") String account);
}

