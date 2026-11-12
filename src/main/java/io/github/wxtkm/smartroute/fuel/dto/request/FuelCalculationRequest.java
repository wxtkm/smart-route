package io.github.wxtkm.smartroute.fuel.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FuelCalculationRequest {

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal currentFuel;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal reserveFuel;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal distanceKm;
}
