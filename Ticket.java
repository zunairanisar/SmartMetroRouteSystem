package com.example.demo1;

import java.io.Serializable;

public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;

    public String passengerName;
    public String passengerCNIC;
    public String startStation;
    public String destStation;
    public int travelTime;
    public String busID;
    public String arrivalTime;
    public String crowdLevel;
    public String paidAmount;
    public String bookingTime;

    public Ticket(String passengerName, String passengerCNIC, String startStation, String destStation,
                  int travelTime, String busID, String arrivalTime, String crowdLevel,
                  String paidAmount, String bookingTime) {
        this.passengerName = passengerName;
        this.passengerCNIC = passengerCNIC;
        this.startStation = startStation;
        this.destStation = destStation;
        this.travelTime = travelTime;
        this.busID = busID;
        this.arrivalTime = arrivalTime;
        this.crowdLevel = crowdLevel;
        this.paidAmount = paidAmount;
        this.bookingTime = bookingTime;
    }
}