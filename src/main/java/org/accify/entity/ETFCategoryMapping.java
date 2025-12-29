package org.accify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "etf_category_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ETFCategoryMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // works with BIGSERIAL
    private Long id;

    @Column(nullable = false, unique = true)
    private String symbol;

    @Column(nullable = false)
    private String category;
}
