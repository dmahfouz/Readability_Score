package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String filePath = args[0];
        String text = readFile(filePath);

        int numWords = countNumberOfWords(text);
        int numChars = countNumberOfChars(text);
        int numSents = countNumberOfSents(text);

        double ariScore = calculateAriScore(numChars, numWords, numSents);
        String ageRange = getSuggestedAgeRange(ariScore);

        System.out.printf("""
                The text is:
                %s
                Words: %d
                Sentences: %d
                Characters: %d
                The score is: %.2f
                This text should be understood by %s year-olds
                """, text, numWords, numSents, numChars, ariScore, ageRange);


    }

    public static String readFile(String filePath) {
        try {
            return readFileAsString(filePath);
        } catch (IOException e) {
            System.out.printf("Error: file %s does not exist!\n", filePath);
            throw new RuntimeException(e);
        }
    }

    private static String readFileAsString(
            String filePath
    ) throws IOException {
        // Read entire file as String
        Path fp = Paths.get(filePath);
        return new String(Files.readAllBytes(fp));
    }

    private static double calculateAriScore(
            int numChars,
            int numWords,
            int numSents
    ) throws ArithmeticException {
        return 4.71 * numChars / numWords +
                0.5 * numWords / numSents - 21.43;

    }

    private static int countNumberOfSents(String text) {
        return text.split("[.!?]").length;
    }

    private static int countNumberOfWords(String text) {
        return text.split("\\s+").length;
    }

    private static int countNumberOfChars(String text) {
        return text.replaceAll("\\s", "").length();
    }

    public static String getSuggestedAgeRange(double ari) {
        // Returns suggested age bracket based on the Automated readability
        // index (ARI) score
        int roundedARI = (int) Math.ceil(ari);

        return switch(roundedARI) {
            case 1 -> "5-6";
            case 2 -> "6-7";
            case 3 -> "7-8";
            case 4 -> "8-9";
            case 5 -> "9-10";
            case 6 -> "10-11";
            case 7 -> "11-12";
            case 8 -> "12-13";
            case 9 -> "13-14";
            case 10 -> "14-15";
            case 11 -> "15-16";
            case 12 -> "16-17";
            case 13 -> "17-18";
            case 14 -> "18-22";
            default -> "N/A";
        };
    }
}
