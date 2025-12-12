package com.example.demo1;

public class RouteManager {


    public static void showRoute(String[] stations) {

        System.out.println("\nMETRO BUS ROUTE");

        for (int i = 0; i < stations.length; i++) {
            System.out.println((i + 1) + ". " + stations[i]);
        }

        System.out.println("===================================");
    }

    public static boolean isValidRoute(String start, String dest, String[] stations) {
        return true;
    }

    public static int calculateTravelTime(String start, String dest, String[] stations) {
        int startIndex = -1;
        int destIndex = -1;

        for (int i = 0; i < stations.length; i++) {
            if (stations[i].equalsIgnoreCase(start))
                startIndex = i;
            if (stations[i].equalsIgnoreCase(dest))
                destIndex = i;
        }

        int stops = Math.abs(destIndex - startIndex);

        return stops * 3;
    }

}