public abstract class FlightDistance {
    public abstract String[] calculateDistance(double lat1, double lon1, double lat2, double lon2);

    // Optional: format distance info (miles/km)
    String formatDistanceInfo(double miles, double km) {
        return String.format("%.2f / %.2f", miles, km);
    }

    public void displayMeasurementInstructions() {
        String symbols = "+---------------------------+";
        System.out.printf("%n%n %100s%n %100s", symbols, "| SOME IMPORTANT GUIDELINES |");
        System.out.printf("%n %100s%n", symbols);
        System.out.println("\n\t\t1. Distance between destinations is based on airport coordinates (latitude & longitude).\n");
        System.out.println("\t\t2. Actual flight distance may vary due to airlines' travel policies or airspace restrictions.\n");
        System.out.println("\t\t3. Flight time depends on factors such as ground speed (assumed 450 knots), aircraft design, altitude, and weather.\n");
        System.out.println("\t\t4. Arrival times may vary by ±1 hour from the schedule; plan accordingly.\n");
        System.out.println("\t\t5. Departure time is when the plane leaves the gate; arrival time is when it reaches the destination gate.\n");
    }
}
