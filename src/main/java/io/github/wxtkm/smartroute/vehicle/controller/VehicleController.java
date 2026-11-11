
package io.github.wxtkm.smartroute.vehicle.controller;

import io.github.wxtkm.smartroute.vehicle.dto.request.CreateVehicleRequest;
import io.github.wxtkm.smartroute.vehicle.dto.request.UpdateVehicleRequest;
import io.github.wxtkm.smartroute.vehicle.dto.response.VehicleResponse;
import io.github.wxtkm.smartroute.vehicle.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponse> createVehicle(
            @Valid @RequestBody CreateVehicleRequest request,
            Authentication authentication
    ) {
        VehicleResponse vehicle = vehicleService.createVehicle(
                request,
                authentication.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehicle);
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getMyVehicles(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                vehicleService.getMyVehicles(authentication.getName())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getMyVehicle(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                vehicleService.getMyVehicle(
                        id,
                        authentication.getName()
                )
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleRequest request,
            Authentication authentication
    ) {
        VehicleResponse updatedVehicle = vehicleService.updateVehicle(
                id,
                request,
                authentication.getName()
        );

        return ResponseEntity.ok(updatedVehicle);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long id,
            Authentication authentication
    ) {
        vehicleService.deleteVehicle(
                id,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}
