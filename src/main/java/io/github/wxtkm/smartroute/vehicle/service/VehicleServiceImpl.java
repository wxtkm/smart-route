
package io.github.wxtkm.smartroute.vehicle.service;

import io.github.wxtkm.smartroute.user.entity.User;
import io.github.wxtkm.smartroute.user.repository.UserRepository;
import io.github.wxtkm.smartroute.vehicle.dto.request.CreateVehicleRequest;
import io.github.wxtkm.smartroute.vehicle.dto.response.VehicleResponse;
import io.github.wxtkm.smartroute.vehicle.entity.Vehicle;
import io.github.wxtkm.smartroute.vehicle.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public VehicleResponse createVehicle(
            CreateVehicleRequest request,
            String userEmail
    ) {
        User user = findUser(userEmail);

        Vehicle vehicle = Vehicle.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .productionYear(request.getProductionYear())
                .fuelConsumption(request.getFuelConsumption())
                .tankCapacity(request.getTankCapacity())
                .user(user)
                .build();

        return toResponse(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getMyVehicles(String userEmail) {
        User user = findUser(userEmail);

        return vehicleRepository.findAllByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponse getMyVehicle(
            Long vehicleId,
            String userEmail
    ) {
        User user = findUser(userEmail);

        Vehicle vehicle = findOwnedVehicle(vehicleId, user.getId());

        return toResponse(vehicle);
    }

    @Override
    @Transactional
    public void deleteVehicle(
            Long vehicleId,
            String userEmail
    ) {
        User user = findUser(userEmail);

        Vehicle vehicle = findOwnedVehicle(vehicleId, user.getId());

        vehicleRepository.delete(vehicle);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
    }

    private Vehicle findOwnedVehicle(Long vehicleId, Long userId) {
        return vehicleRepository.findByIdAndUserId(vehicleId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vehicle not found"
                ));
    }

    private VehicleResponse toResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .productionYear(vehicle.getProductionYear())
                .fuelConsumption(vehicle.getFuelConsumption())
                .tankCapacity(vehicle.getTankCapacity())
                .build();
    }
}
