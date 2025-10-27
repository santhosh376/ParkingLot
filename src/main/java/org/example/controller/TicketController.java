package org.example.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.dtos.IssueTicketRequest;
import org.example.dtos.IssueTicketResponse;
import org.example.model.Ticket;
import org.example.model.VehicleType;
import org.example.services.TicketService;


public class TicketController {

    private TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public IssueTicketResponse issueTicket(IssueTicketRequest request){

        Ticket ticket = null;
        try {
            ticket = ticketService.issueTicket(
                    request.getVehicleNumber(),
                    request.getVehicleType(),
                    request.getGateId());
        } catch (Exception e) {
            throw new RuntimeException("INVALID GATE");
        }


        return IssueTicketResponse.builder()
                .ticketId(ticket.getNumber())
                .floorNumber(ticket.getParkingSpot().getParkingFloor().getFloorNumber())
                .entryTime(ticket.getEntryTime())
                .vehicleNumber(ticket.getVehicle().getVehicleNumber())
                .spotNumber(ticket.getParkingSpot().getSpotNumber())
                .gateNumber(ticket.getGeneratedAt().getGateNumber())
                .build();



    }

}
