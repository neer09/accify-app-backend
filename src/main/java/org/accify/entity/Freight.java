package org.accify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "freight")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Freight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String consignor;
    private String consignee;
    private String fromLocation;
    private String toLocation;
    private String vehicleNo;
    private Double weight;
    private Double amount;
    private Double gst;
    private Double total;
}
