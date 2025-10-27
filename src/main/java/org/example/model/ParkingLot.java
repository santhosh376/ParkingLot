package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ParkingLot {
    private String name;
    private String address;
    private List<ParkingFloor> parkingFloorList;
    private List<Gate> entryGates;
    private List<Gate> exitGates;


}
