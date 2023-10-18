import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GymSystem {    //När användaren anger antingen personnummer eller namn, försöker programmet matcha det med informationen i customer.txt. Om en matchning hittas,
    // klassificeras medlemmen och en träningspost sparas i TrainerFile.txt.

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

    private static void processCustomer(String input) throws IOException {   // tar emot användar input och läser innehållet från customer fil för att hitta en matchning.
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE_PATH))) {  //try-with-resources används för att säkerställa att filresurser stängs korrekt efter användning.
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    String personalNumber = parts[0];
                    String name = parts[1];
                    LocalDate lastPaymentDate = parseDate(reader.readLine());

                    if (personalNumber.equals(input) || name.equals(input)) {  // om en matchning hittas så används denna metod för att klacifiera medlem och spara det i trainerfile.
                        classifyAndPrintMember(lastPaymentDate);
                        saveTrainerRecord(name, personalNumber, LocalDate.now());
                        return;
                    }
                } else {
                    System.out.println("Invalid entry in customer file: " + line);
                }
            }

            // om personen ej hittas i våran customer fil.
            System.out.println("Person not found in the customer file. Unauthorized access!");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Customer file not found. Please check the file path."); //specifik information om eventuella fel vid inläsning av fil.
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred while processing the customer file");
            e.printStackTrace();
        }
    }

    private static void classifyAndPrintMember(LocalDate lastPaymentDate) { //denna metod klassificiera en medlem som antingen current ellerr former member beroende på senast betalnignsdatum.
        LocalDate currentDate = LocalDate.now();
        if (currentDate.minusYears(1).isBefore(lastPaymentDate)) {
            System.out.println("Current member.");
        } else {
            System.out.println("Former member.");
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