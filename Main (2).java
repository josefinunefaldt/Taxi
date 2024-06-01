import java.util.Scanner;

/**
 * @author Josefin Unefäldt (josune-3)
 */
public class Main {
    public static final int MENU_ITEM_1 = 1;
    public static final int MENU_ITEM_2 = 2;
    public static final int MENU_ITEM_3 = 3;
    public static final int MENU_ITEM_4 = 4;
    public static final int MENU_ITEM_5 = 5;
    public static final int MENU_ITEM_Q = -1;
    public static final int MAX_CARS = 25;
    public static final int FLEET_COLUMN_SIZE = 3;
    public static final int RIDE_COLUMN_SIZE = 4;
    public static final int KILOMETER_COST = 35;
    public static final int START_COST = 200;
    public static final String ERROR_MESSAGE = "Invald input. Please try again.";

    private static Scanner userInputScanner = new Scanner(System.in);

    public static void main(String[] args) {
        String[][] taxiArray = new String[MAX_CARS][FLEET_COLUMN_SIZE];
        String[][] taxiRideArray = new String[MAX_CARS][RIDE_COLUMN_SIZE];
        int numOfTaxi = 0;
        int numOfRiders = 0;

        while (true) {
            int userSelection = menu();
            switch (userSelection) {
                case MENU_ITEM_1:
                    numOfTaxi = addCarToFleet(taxiArray, numOfTaxi);
                    break;
                case MENU_ITEM_2:
                    numOfRiders = startRide(taxiArray, numOfTaxi, taxiRideArray, numOfRiders);
                    break;
                case MENU_ITEM_3:
                    numOfRiders = endRide(taxiArray, numOfTaxi, taxiRideArray, numOfRiders);
                    break;
                case MENU_ITEM_4:
                    printFleet(taxiArray, numOfTaxi);
                    break;
                case MENU_ITEM_5:
                    printRideSummary(taxiRideArray, numOfRiders);
                    break;
                case MENU_ITEM_Q:
                    System.out.println("Thank you for using the Taxi Service Program.");
                    System.exit(0);
                default:
                    System.out.println(ERROR_MESSAGE);
                    break;
            }
        }
    }

    public static int menu() {
        System.out.println("----------------------------------");
        System.out.println("# LTU Taxi");
        System.out.println("----------------------------------");
        System.out.println("1. Add car to fleet");
        System.out.println("2. Start a ride");
        System.out.println("3. End a ride");
        System.out.println("4. Print car fleet");
        System.out.println("5. Print ride summary");
        System.out.println("q. End program");
        System.out.print("> Enter your option: ");
        return input();
    }

    public static int input() {
        while (true) {
            if (userInputScanner.hasNextInt()) {
                int input = userInputScanner.nextInt();
                userInputScanner.nextLine();
                return input;
            } else {
                String userInput = userInputScanner.next();
                if (userInput.equals("q")) {
                    return MENU_ITEM_Q;
                } else {
                    System.out.println(ERROR_MESSAGE);
                }

            }
        }
    }

    public static boolean isValidRegistrationNumber(String registrationNumber) {
        return registrationNumber.matches("^[A-Z]{3}\\d{3}$"); // regex to check if registration number is valid
    }

    public static boolean isUniqueRegistrationNumber(String registrationNumber, String[][] taxiArray, int numOfTaxi) {
        for (int i = 0; i < numOfTaxi; i++) {
            if (taxiArray[i][0].equals(registrationNumber)) {
                return false; // checking if theres already a car with the same registration number
            }
        }
        return true;
    }

    public static int addCarToFleet(String[][] taxiArray, int numOfTaxi) {
        System.out.print("Enter registration number: ");
        String registrationNumber = userInputScanner.next();

        if (!isValidRegistrationNumber(registrationNumber)) {
            System.out.println(
                    "Invalid registration number. It should start with three capital letters followed by three digits.");
            return numOfTaxi;
        }

        if (!isUniqueRegistrationNumber(registrationNumber, taxiArray, numOfTaxi)) {
            System.out.println("Registration number is already in use. Please enter a unique registration number.");
            return numOfTaxi;
        }

        System.out.print("> Enter make and model: ");
        String makeAndModel = userInputScanner.next();
        if (numOfTaxi < MAX_CARS) {
            taxiArray[numOfTaxi][0] = registrationNumber;
            taxiArray[numOfTaxi][1] = makeAndModel;
            taxiArray[numOfTaxi][2] = "Available";
            numOfTaxi++;
            System.out.println(
                    makeAndModel + " with registration number " + registrationNumber + " was added to car fleet.");
        } else {
            System.out.println("Fleet is full. Cannot add more cars.");
        }

        return numOfTaxi;
    }

    public static int startRide(String[][] taxiArray, int numOfTaxi, String[][] taxiRideArray, int numOfRiders) {
        System.out.println("> Enter car's registration number: ");
        String registrationNumber = userInputScanner.next();

        if (!isValidRegistrationNumber(registrationNumber)) {
            System.out.println(
                    "Invalid registration number. It should start with three capital letters followed by three digits.");
            return numOfRiders;
        } else {
            int taxiIndex = -1;
            for (int i = 0; i < numOfTaxi; i++) {
                if (taxiArray[i][0].equals(registrationNumber)) {
                    taxiIndex = i;
                    break;
                }
            }

            if (taxiIndex == -1) {
                System.out.println("There is no car with registration number " + registrationNumber + " in the fleet.");
                return numOfRiders;
            }

            if (taxiArray[taxiIndex][2].equals("Taken")) {
                System.out.println("Car with registration number " + registrationNumber + " is already in use.");
                return numOfRiders;
            }

            System.out.println("> Enter pickup address:");
            String pickupAddress = userInputScanner.next();
            System.out.println("> Enter rider's name:");
            String passengerName = userInputScanner.next();

            taxiRideArray[taxiIndex][0] = passengerName;
            taxiRideArray[taxiIndex][1] = registrationNumber;
            taxiRideArray[taxiIndex][2] = null;
            taxiRideArray[taxiIndex][3] = null;
            numOfRiders++;
            System.out.printf("Taxi with registration number %s picked up %s at %s%n", registrationNumber,
                    passengerName, pickupAddress);

            taxiArray[taxiIndex][2] = "Taken";

        }

        return numOfRiders;
    }

    public static int endRide(String[][] taxiArray, int numOfTaxi, String[][] taxiRideArray, int numOfRiders) {
        double distance = 0;
        double cost = 0;
        String formattedCost = null;
        String formattedDistance = null;
        double finalCost = 0;
        System.out.println("> Enter registration number: ");
        String registrationNumber = userInputScanner.next();
        if (!isValidRegistrationNumber(registrationNumber)) {
            System.out.println(
                    "Invalid registration number. It should start with three capital letters followed by three digits.");
            return numOfRiders;
        }

        // Find the index of the taxi with the given registration number
        int taxiIndex = -1;
        for (int i = 0; i < numOfTaxi; i++) {
            if (taxiArray[i][0].equals(registrationNumber)) {
                taxiIndex = i;
                break;
            }
        }

        if (taxiIndex == -1) {
            System.out.println("There is no car with registration number " + registrationNumber + " in the fleet.");
            return numOfRiders;
        }

        if (taxiArray[taxiIndex][2].equals("Taken")) {

            System.out.println("> Distance covered in km:");
            String distanceCovered = userInputScanner.next();
            double distanceCost = Double.parseDouble(distanceCovered) * KILOMETER_COST; // converting the cost to count
            finalCost = START_COST + distanceCost;
            String finalCostString = String.valueOf(finalCost); // converting back to fit in string array
            taxiRideArray[taxiIndex][3] = finalCostString;
            taxiRideArray[taxiIndex][2] = distanceCovered;
            taxiArray[taxiIndex][2] = "Available";

            distance = Double.parseDouble(taxiRideArray[taxiIndex][2]);
            cost = Double.parseDouble(taxiRideArray[taxiIndex][3]);

            formattedDistance = String.format("%.2f", distance);
            formattedCost = String.format("%.2f", cost);

            System.out.println("===================================");
            System.out.println("LTU Taxi");
            System.out.println("===================================");
            System.out.printf("Name: %s %n", taxiRideArray[taxiIndex][0]);
            System.out.printf("Car: %s (%s) %n", taxiArray[taxiIndex][1], taxiRideArray[taxiIndex][1]);
            System.out.printf("Distance: %s %n", formattedDistance);
            System.out.printf("Cost: %s %n", formattedCost);
        }

        return numOfRiders;

    }

    public static void printFleet(String[][] taxiArray, int numOfTaxi) {
        int availableCounter = 0;
        int takencounter = 0;
        int totalCars = 0;
        System.out.println("LTU Taxi car fleet:");
        System.out.println("Fleet:");

        System.out.printf("%-10s %-15s %-10s %n", "Model", "Numberplate", "Status");
        for (int i = 0; i < numOfTaxi; i++) {
            System.out.printf("%-10s %-15s %-10s %n", taxiArray[i][0], taxiArray[i][1], taxiArray[i][2]);

            if (taxiArray[i][2].equals("Available")) {
                availableCounter++;
            }
            if (taxiArray[i][2].equals("Taken")) {
                takencounter++;
            }
            totalCars = availableCounter + takencounter;
        }
        System.out.printf("Total number of cars: %d%n", totalCars);
        System.out.printf("Total number of available cars: %d%n", availableCounter);

    }

    public static void printRideSummary(String[][] taxiRideArray, int numOfriders) {
        System.out.println("LTU Taxi ride summary:");
        System.out.println("Rides:");
        double revenue = 0;
        double distance = 0;
        double cost = 0;
        String formattedCost = "";
        String formattedDistance = "";

        for (int i = 0; i < numOfriders - 1; i++) {
            for (int j = 0; j < numOfriders - 1 - i; j++) {
                if (taxiRideArray[j][0] != null && taxiRideArray[j + 1][0] != null) {
                    if (taxiRideArray[j][0].compareTo(taxiRideArray[j + 1][0]) > 0) {
                        String[] temp = taxiRideArray[j];
                        taxiRideArray[j] = taxiRideArray[j + 1];
                        taxiRideArray[j + 1] = temp;
                    }

                }
            }
        }

        System.out.printf("%-10s %-15s %-10s %-10s%n", "Name", "Numberplate", "Distance", "Cost");
        for (int i = 0; i < numOfriders; i++) {
            if (taxiRideArray[i][3] != null) {
                revenue += Double.parseDouble(taxiRideArray[i][3]);

                // Parse distance and cost to doubles
                distance = Double.parseDouble(taxiRideArray[i][2]);
                cost = Double.parseDouble(taxiRideArray[i][3]);

                // Format distance and cost with two decimals
                formattedDistance = String.format("%.2f km", distance);
                formattedCost = String.format("%.2f SEK", cost);

                System.out.printf("%-10s %-15s %-10s %-10s%n", taxiRideArray[i][0], taxiRideArray[i][1],
                        formattedDistance,
                        formattedCost);

            }
        }

        System.out.println("Total number of rides: " + numOfriders);
        System.out.printf("Total revenue: %.2f SEK%n", revenue);
    }

}
