package org.example;

import org.example.controller.TicketController;
import org.example.dtos.IssueTicketRequest;
import org.example.dtos.IssueTicketResponse;
import org.example.model.VehicleType;
import org.example.repositories.GateRepository;
import org.example.repositories.ParkingLotRepository;
import org.example.repositories.TicketRepository;
import org.example.repositories.VehicleRepository;
import org.example.services.TicketService;
import org.example.strategies.RandomSpotAssignmentStrategy;
import org.example.strategies.SpotAssignmentStrategy;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        GateRepository gateRepository = new GateRepository();
        ParkingLotRepository parkingLotRepository = new ParkingLotRepository();
        TicketRepository ticketRepository = new TicketRepository();
        VehicleRepository vehicleRepository = new VehicleRepository();

        SpotAssignmentStrategy spotAssignmentStrategy = new RandomSpotAssignmentStrategy(parkingLotRepository);

        TicketService ticketService = new TicketService(gateRepository, vehicleRepository,spotAssignmentStrategy, ticketRepository);

        TicketController ticketController = new TicketController(ticketService);

        IssueTicketResponse response = ticketController.issueTicket(IssueTicketRequest.builder()
                                                            .gateId(1L)
                                                            .vehicleNumber("xyz")
                                                            .vehicleType(VehicleType.CAR)
                                                            .build());
    }
}