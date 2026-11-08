
package io.github.wxtkm.smartroute.vehicle.entity;

import io.github.wxtkm.smartroute.user.entity.User;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    private Integer productionYear;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal fuelConsumption;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal tankCapacity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
