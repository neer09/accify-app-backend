package org.accify.repo;

import org.accify.entity.ETFWatchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ETFWatchlistRepository extends JpaRepository<ETFWatchlist, Long> {
    Optional<ETFWatchlist> findBySymbol(String symbol);
    boolean existsBySymbol(String symbol);
}
