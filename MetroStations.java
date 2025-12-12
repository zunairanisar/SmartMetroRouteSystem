package com.example.demo1;
public class MetroStations {

    public static String[] stations = {
            "Gajju Mata",
            "NISP",
            "Amna Plaza",
            "Attari Saroba",
            "Nazaria-e-Pakistan",
            "Rehmanabad",
            "Ghazi Chowk",
            "Chungi Amar Sidhu",
            "Ittefaq Hospital",
            "Model Town",
            "Qainchi",
            "Kot Lakhpat",
            "Peco Road",
            "Canal Road",
            "Qartaba Chowk",
            "MAO College",
            "Civil Secretariat",
            "Anarkali",
            "Bhatti Chowk",
            "Texali Gate",
            "Shahdara"
    };

    public static boolean isValidStation(String station) {
        for (String s : stations) {
            if (s.equalsIgnoreCase(station.trim())) return true;
        }
        return false;
    }

    public static void showStations() {
        System.out.println("\n===== Lahore Metro Bus Stations =====");
        for (int i = 0; i < stations.length; i++) {
            System.out.println((i + 1) + ". " + stations[i]);
        }
        System.out.println("=====================================");
    }
}