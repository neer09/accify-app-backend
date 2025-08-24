package org.accify.repo;

import org.accify.entity.Freight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FreightRepository extends JpaRepository<Freight, Long> {

    @Query("SELECT COUNT(f) FROM Freight f")
    int countAll();
}
