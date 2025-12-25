package org.accify.repo;

import org.accify.entity.ETF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ETFRepository extends JpaRepository<ETF, Long> {
    List<ETF> findByEnabledTrue();
}

