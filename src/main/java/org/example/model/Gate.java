package org.example.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Gate {
    private int gateNumber;
    private Operator operator;
    private GateType gateType;

}
