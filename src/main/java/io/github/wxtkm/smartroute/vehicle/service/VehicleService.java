
package io.github.wxtkm.smartroute.vehicle.service;

import io.github.wxtkm.smartroute.vehicle.dto.request.CreateVehicleRequest;
import io.github.wxtkm.smartroute.vehicle.dto.request.UpdateVehicleRequest;
import io.github.wxtkm.smartroute.vehicle.dto.response.VehicleResponse;

import java.util.List;

public interface VehicleService {

    VehicleResponse createVehicle(
            CreateVehicleRequest request,
            String userEmail
    );

    List<VehicleResponse> getMyVehicles(String userEmail);

    VehicleResponse getMyVehicle(Long vehicleId, String userEmail);


    VehicleResponse updateVehicle(
            Long vehicleId,
            UpdateVehicleRequest request,
            String userEmail
    );


    void deleteVehicle(Long vehicleId, String userEmail);
}
