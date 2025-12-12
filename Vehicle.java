package com.example.demo1;
public class Vehicle {

    private String vehicleID;
    private String[] routeStations;
    private String arrivalTime;

    private int totalCapacity;
    private int filled;


    public Vehicle(String vehicleID, String[] routeStations, String arrivalTime, int totalCapacity) {
        this.vehicleID = vehicleID;
        this.routeStations = routeStations;
        this.arrivalTime = arrivalTime;
        this.totalCapacity = totalCapacity;
        this.filled = 0;
    }

    public boolean bookSeat() {

        if (filled >= totalCapacity) {
            return false;
        }
        filled++;
        return true;
    }


    public String getCrowdLevel() {

        double percent = (filled * 100.0) / totalCapacity;

        if (percent == 100)
            return "FULL";

        else if (percent >= 70)
            return "HIGH";

        else if (percent >= 40)
            return "MEDIUM";

        else
            return "LOW";
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public String[] getRouteStations() {
        return routeStations;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public int getRemainingSeats() {
        return totalCapacity - filled;
    }

    public int getFilledSeats() {
        return filled;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }



    public void showInfo() {

        System.out.println("\n===== VEHICLE INFO =====");
        System.out.println("Bus ID: " + vehicleID);
        System.out.println("Arrival Time: " + arrivalTime);
        System.out.println("Capacity: " + filled + " / " + totalCapacity);
        System.out.println("Crowd Level: " + getCrowdLevel());

        System.out.print("Route: ");
        for (int i = 0; i < routeStations.length; i++) {
            System.out.print(routeStations[i]);
            if (i < routeStations.length - 1) System.out.print(" -> ");
        }
        System.out.println("\n========================");
    }

}