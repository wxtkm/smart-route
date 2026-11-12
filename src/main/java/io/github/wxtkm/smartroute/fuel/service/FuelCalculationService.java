package io.github.wxtkm.smartroute.fuel.service;

import io.github.wxtkm.smartroute.fuel.dto.request.FuelCalculationRequest;
import io.github.wxtkm.smartroute.fuel.dto.response.FuelCalculationResponse;
import io.github.wxtkm.smartroute.user.entity.User;
import io.github.wxtkm.smartroute.user.repository.UserRepository;
import io.github.wxtkm.smartroute.vehicle.entity.Vehicle;
import io.github.wxtkm.smartroute.vehicle.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class FuelCalculationService {

    private static final BigDecimal HUNDRED =
            BigDecimal.valueOf(100);

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public FuelCalculationResponse calculate(
            Long vehicleId,
            FuelCalculationRequest request,
            String userEmail
    ) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Vehicle vehicle = vehicleRepository
                .findByIdAndUserId(vehicleId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vehicle not found"
                ));

        BigDecimal consumption = vehicle.getFuelConsumption();
        BigDecimal tankCapacity = vehicle.getTankCapacity();

        BigDecimal currentFuel = request.getCurrentFuel();
        BigDecimal reserveFuel = request.getReserveFuel();
        BigDecimal distance = request.getDistanceKm();

        if (currentFuel.compareTo(tankCapacity) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Current fuel exceeds tank capacity"
            );
        }

        if (reserveFuel.compareTo(currentFuel) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Reserve fuel exceeds current fuel"
            );
        }

        BigDecimal availableFuel =
                currentFuel.subtract(reserveFuel);

        BigDecimal maxDistance = availableFuel
                .multiply(HUNDRED)
                .divide(consumption, 2, RoundingMode.HALF_UP);

        BigDecimal requiredFuel = distance
                .multiply(consumption)
                .divide(HUNDRED, 2, RoundingMode.HALF_UP);

        BigDecimal fuelAfterTrip =
                currentFuel.subtract(requiredFuel);

        boolean canReach = distance
                .multiply(consumption)
                .compareTo(availableFuel.multiply(HUNDRED)) <= 0;

        return FuelCalculationResponse.builder()
                .vehicleId(vehicle.getId())
                .fuelConsumption(consumption)
                .currentFuel(currentFuel)
                .reserveFuel(reserveFuel)
                .availableFuel(availableFuel)
                .maxDistanceKm(maxDistance)
                .distanceKm(distance)
                .requiredFuel(requiredFuel)
                .fuelAfterTrip(fuelAfterTrip)
                .canReach(canReach)
                .build();
    }
}
