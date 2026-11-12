package io.github.wxtkm.smartroute.fuel.controller;

import io.github.wxtkm.smartroute.fuel.dto.request.FuelCalculationRequest;
import io.github.wxtkm.smartroute.fuel.dto.response.FuelCalculationResponse;
import io.github.wxtkm.smartroute.fuel.service.FuelCalculationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class FuelController {

    private final FuelCalculationService fuelCalculationService;

    @PostMapping("/{id}/range/calculate")
    public ResponseEntity<FuelCalculationResponse> calculateRange(
            @PathVariable Long id,
            @Valid @RequestBody FuelCalculationRequest request,
            Authentication authentication
    ) {

        FuelCalculationResponse result =
                fuelCalculationService.calculate(
                        id,
                        request,
                        authentication.getName()
                );

        return ResponseEntity.ok(result);
    }
}
