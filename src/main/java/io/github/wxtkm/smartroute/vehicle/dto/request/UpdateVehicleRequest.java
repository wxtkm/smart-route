
package io.github.wxtkm.smartroute.vehicle.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateVehicleRequest {

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
