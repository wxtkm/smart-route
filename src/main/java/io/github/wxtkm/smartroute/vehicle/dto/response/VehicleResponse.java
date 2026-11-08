
package io.github.wxtkm.smartroute.vehicle.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class VehicleResponse {

    private Long id;
    private String brand;
    private String model;
    private Integer productionYear;
    private BigDecimal fuelConsumption;
    private BigDecimal tankCapacity;
}
