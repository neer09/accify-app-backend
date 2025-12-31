package org.accify.repo;

import org.accify.entity.ETFSellNotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ETFSellNotificationLogRepository extends JpaRepository<ETFSellNotificationLog, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ETFSellNotificationLog")
    void deleteAllLogs();
}

