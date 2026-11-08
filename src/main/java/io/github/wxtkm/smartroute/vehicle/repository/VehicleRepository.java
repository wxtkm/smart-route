
package io.github.wxtkm.smartroute.vehicle.repository;

import io.github.wxtkm.smartroute.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findAllByUserId(Long userId);

    Optional<Vehicle> findByIdAndUserId(Long id, Long userId);
}
