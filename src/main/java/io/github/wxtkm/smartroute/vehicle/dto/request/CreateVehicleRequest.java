
package io.github.wxtkm.smartroute.vehicle.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateVehicleRequest {

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @Min(1886)
    @Max(2100)
    private Integer productionYear;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal fuelConsumption;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal tankCapacity;
}
