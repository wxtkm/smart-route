package io.github.wxtkm.smartroute.fuel.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FuelCalculationResponse {

    private Long vehicleId;

    private BigDecimal fuelConsumption;
    private BigDecimal currentFuel;
    private BigDecimal reserveFuel;

    private BigDecimal availableFuel;
    private BigDecimal maxDistanceKm;

    private BigDecimal distanceKm;
    private BigDecimal requiredFuel;
    private BigDecimal fuelAfterTrip;

    private boolean canReach;
}
