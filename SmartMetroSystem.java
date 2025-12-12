package com.example.demo1;
import java.util.Scanner;

public class SmartMetroSystem {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String[] stations = MetroStations.stations;

        Vehicle bus1 = new Vehicle("BUS-1001", stations, "5 minutes", 20);
        Vehicle bus2 = new Vehicle("BUS-2001", stations, "10 minutes", 25);
        Vehicle bus3 = new Vehicle("BUS-3001", stations, "15 minutes", 30);

        Vehicle[] buses = {bus1, bus2, bus3};

        while (true) {

            System.out.println("\n===== METRO TICKET BOOKING SYSTEM =====");

            String name = "";
            String cnic = "";

            System.out.print("Enter Your Name: ");
            name = sc.nextLine();

            while (true) {
                System.out.print("Enter Your CNIC (13 digits): ");
                cnic = sc.nextLine();

                if (cnic.length() == 13 && cnic.matches("[0-9]+"))
                    break;

                System.out.println("❌ Invalid CNIC! Enter again.");
            }



            System.out.println("\n===== Lahore Metro Bus Stations =====");
            for (int i = 0; i < stations.length; i++) {
                System.out.println((i + 1) + ". " + stations[i]);
            }
            System.out.println("=====================================");

            System.out.print("\nEnter Start Station: ");
            String start = sc.nextLine();


            System.out.print("Enter Destination Station: ");
            String dest = sc.nextLine();


            if (!RouteManager.isValidRoute(start, dest, stations)) {
                System.out.println("\n❌ Invalid Route! Try again.");
                continue;
            }

            int travelTime = RouteManager.calculateTravelTime(start, dest, stations);

            Vehicle selectedBus = null;

            for (int i = 0; i < buses.length; i++) {

                if (buses[i].bookSeat()) {
                    selectedBus = buses[i];
                    break;
                }
            }

            if (selectedBus == null) {
                System.out.println("\n❌ All buses are FULL! No seat available.");
                continue;
            }

            System.out.println("\nPayment Options:");
            System.out.println("1. Cash");
            System.out.println("2. Online Payment (EasyPaisa / JazzCash / Card)");
            System.out.print("Choose payment method: ");
            int payChoice = sc.nextInt();
            sc.nextLine();

            String paymentStatus = "";

            if (payChoice == 1) {
                paymentStatus = "Cash Payment - Completed";
            } else {
                System.out.print("Enter Mobile Number for Online Payment: ");
                String mobile = sc.nextLine();

                System.out.print("Enter Amount to Pay: ");
                String amount = sc.nextLine();

                paymentStatus = "Online Payment Successful (Mobile: " + mobile + ")";
            }

            System.out.println("\n=========== TICKET RECEIPT ===========");
            System.out.println("Passenger Name: " + name);
            System.out.println("CNIC: " + cnic);

            System.out.println("Start Station: " + start);
            System.out.println("Destination: " + dest);
            System.out.println("Travel Time: " + travelTime + " minutes");

            System.out.println("--------------------------------------");
            System.out.println("Bus ID: " + selectedBus.getVehicleID());
            System.out.println("Arrival Time: " + selectedBus.getArrivalTime());
            System.out.println("Crowd Level: " + selectedBus.getCrowdLevel());
            System.out.println("Seats Filled: " + selectedBus.getFilledSeats() +
                    "/" + selectedBus.getTotalCapacity());
            System.out.println("--------------------------------------");

            System.out.println("Payment Status: " + paymentStatus);
            System.out.println("Ticket Booked Successfully!");
            System.out.println("======================================");

            System.out.print("\nDo you want to book another ticket? (yes/no): ");
            String again = sc.nextLine();

            if (!again.equalsIgnoreCase("yes")) {
                System.out.println("\nThank you for using Smart Metro System!");
                break;
            }
        }

    }
}