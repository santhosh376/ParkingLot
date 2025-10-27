package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingSpot {
    private int spotNumber;
    private VehicleType vehicleType;
    private ParkingFloor parkingFloor;
    private ParkingSpotStatus parkingSpotStatus;
    private Vehicle vehicle;
}
