package org.example.services;

import org.example.exceptions.GateNotFoundException;
import org.example.model.*;
import org.example.repositories.GateRepository;
import org.example.repositories.TicketRepository;
import org.example.repositories.VehicleRepository;
import org.example.strategies.SpotAssignmentStrategy;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

public class  TicketService {

    private final GateRepository gateRepository;
    private final VehicleRepository vehicleRepository;
    private final SpotAssignmentStrategy spotAssignmentStrategy;
    private final TicketRepository ticketRepository;

    public TicketService(GateRepository gateRepository, VehicleRepository vehicleRepository,SpotAssignmentStrategy spotAssignmentStrategy,TicketRepository ticketRepository) {
        this.gateRepository = gateRepository;
        this.vehicleRepository = vehicleRepository;
        this.spotAssignmentStrategy = spotAssignmentStrategy;
        this.ticketRepository = ticketRepository;
    }

    public Ticket issueTicket(String vehicleNumber, VehicleType vehicleType, Long gateId) throws GateNotFoundException {

        //Query the database to get the objects using the ID
        Ticket ticket = new Ticket();
        Optional<Gate> gateOptional = gateRepository.findGateById(gateId);

//        if(gateOptional.isEmpty()){
//            throw new GateNotFoundException();
//        }
//
//        Gate gate = gateOptional.get();

        Gate gate = gateOptional.orElseThrow(GateNotFoundException::new);

        Vehicle savedVehicle;
        Optional<Vehicle> optionalVehicle = vehicleRepository.findVehicleById(vehicleNumber);
        if(optionalVehicle.isEmpty()){
            Vehicle vehicle = new Vehicle();
            vehicle.setVehicleNumber(vehicleNumber);
            vehicle.setVehicleType(vehicleType);
            savedVehicle = vehicle;
        }else{
            savedVehicle = optionalVehicle.get();
        }

        ticket.setVehicle(savedVehicle);
        ticket.setGeneratedAt(gate);
        ticket.setEntryTime(new Date());
        ticket.setGeneratedBy(gate.getOperator());
        ticket.setNumber(String.valueOf(Instant.now().getEpochSecond()));
//        ticket.setParkingSpot();

        ParkingSpot parkingSpot = spotAssignmentStrategy.getSpot(1L, gate, vehicleType);

        parkingSpot.setVehicle(savedVehicle);
        parkingSpot.setParkingSpotStatus(ParkingSpotStatus.OCCUPIED);

        ticket.setParkingSpot(parkingSpot);

        ticketRepository.save(ticket);

        return ticket;


        //Save the ticket object and vehicle number in the DB  -> repository

        //1. Create a  ticket object
        //2. Select the spot for vehicle
        //3. return object
    }
}
