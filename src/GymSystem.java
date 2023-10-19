import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GymSystem {

    private static final String TRAINER_FILE_PATH = "src/TrainerFile.txt"; //relativ path för våra två konstanta strängar, dom innehåller filvägar till två textfiler
    private static final String CUSTOMER_FILE_PATH = "src/customer.txt";

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter customer's personal number or name: ");
            String input = scanner.nextLine();

            if (input.trim().isEmpty()){
                System.out.println("Invalid input. Please neter a valid personal number or name");
                return;
            }
            processCustomer(input);
        } catch (IOException e) { //felhantering
            e.printStackTrace();
            System.out.println("An error occurred while processing the request.");
        }
    }

    private static void processCustomer(String input) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    String personalNumber = parts[0];
                    String name = parts[1];
                    LocalDate lastPaymentDate = parseDate(reader.readLine());

                    if (personalNumber.equals(input) || name.equals(input)) {
                        boolean isCurrentMember = classifyAndPrintMember(lastPaymentDate);

                        if (isCurrentMember) {
                            saveTrainerRecord(name, personalNumber, LocalDate.now());
                        }
                        return;
                    }
                } else {
                    System.out.println("Invalid entry in customer file: " + line);
                }
            }

            System.out.println("Person not found in the customer file. Unauthorized access!");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Customer file not found. Please check the file path.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred while processing the customer file");
            e.printStackTrace();
        }
    }


    private static boolean classifyAndPrintMember(LocalDate lastPaymentDate) { //denna metod klassificiera en medlem som antingen current ellerr former member beroende på senast betalnignsdatum.
        LocalDate currentDate = LocalDate.now();
        if (currentDate.minusYears(1).isBefore(lastPaymentDate)) {
            System.out.println("Current member.");

            return true;
        } else {
            System.out.println("Former member.");
            return false;
        }
    }

    private static void saveTrainerRecord(String name, String personalNumber, LocalDate trainingDate) throws IOException { //denna metod sparar medlemar i filen trainerfile, den tar in namn, PN och träningsdatum som parametrar och skriver in dem i filen.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRAINER_FILE_PATH, true))) {
            String record = name + ", " + personalNumber + ", " + trainingDate;
            writer.write(record);
            writer.newLine();
            System.out.println("Training record saved for " + name);
        }
    }

    private static LocalDate parseDate(String dateString) { // denna metod används för att konvertera en strängrepresentation av ett datum som läses från filen till ett ¨localdate¨ objet, detta görs med hjälp av datetimeformatter.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");  //använder DateTimeFormatter för att bestämma korrekt mönster.
        return LocalDate.parse(dateString, formatter);
    }
}