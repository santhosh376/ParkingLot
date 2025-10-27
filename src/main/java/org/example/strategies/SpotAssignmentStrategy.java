package org.example.strategies;

import org.example.model.Gate;
import org.example.model.ParkingSpot;
import org.example.model.VehicleType;

public interface SpotAssignmentStrategy {

    public ParkingSpot getSpot(Long parkingLotId,Gate gate, VehicleType vehicleType);
}
