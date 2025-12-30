package org.accify.repo;

import org.accify.entity.ETFCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ETFCategoryMappingRepository extends JpaRepository<ETFCategoryMapping, Long> {
    ETFCategoryMapping findBySymbol(String symbol);
}
