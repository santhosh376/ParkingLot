package org.example.repositories;

import org.example.model.Ticket;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TicketRepository {
    private Map<Long , Ticket> db = new HashMap<>();

    public Optional<Ticket> findTicketById(Long id){
        if(db.containsKey(id)){
            return Optional.of(db.get(id));
        }
        return Optional.empty();
    }

    public void save(Ticket ticket){
        db.put(Long.valueOf(ticket.getNumber()),ticket);
    }
}
