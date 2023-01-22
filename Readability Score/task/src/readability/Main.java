package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String filePath = args[0];
        String text = readFile(filePath);
        readabilitySummary(text);
    }

    public static void readabilitySummary(String text) {

        int numWords = countNumberOfWords(text);
        int numSents = countNumberOfSents(text);
        int numChars = countNumberOfChars(text);
        int numSyllables = countNumberOfSyllables(text);
        int numPolysyllables = countNumberOfPolysyllables(text);

        System.out.printf("The text is:\n%s\n\n", text);
        System.out.printf("Words: %d%n", numWords);
        System.out.printf("Sentences: %d%n", numSents);
        System.out.printf("Characters: %d%n", numChars);
        System.out.printf("Syllables: %d%n", numSyllables);
        System.out.printf("Polysyllables: %d%n", numPolysyllables);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        System.out.println();

        String choice = scanner.next();

        switch (choice) {
            case "ARI", "FK", "SMOG", "CL" -> {
                System.out.println(readabilityResult(choice, text));
                System.out.printf("This text should be understood in average by %d-year-olds.\n",
                        getSuggestedAge(readabilityScore(choice, text)));
            }
            case "all" -> {
                System.out.println(readabilityResult("ARI", text));
                System.out.println(readabilityResult("FK", text));
                System.out.println(readabilityResult("SMOG", text));
                System.out.println(readabilityResult("CL", text));
                System.out.printf("This text should be understood in average by %.2f-year-olds.",
                        averageReadingAge(text));
            }
            default -> throw new IllegalStateException(String.format("Invalid choice: %s", choice));
        }

    }

    public static String readabilityResult(String choice, String text) {
        String method = readabilityMethod(choice);
        double score = readabilityScore(choice, text);
        int ageRange = getSuggestedAge(score);
        return String.format("%s: %.2f (about %d-year-olds)", method, score, ageRange);

    }

    public static double averageReadingAge(String text) {
        double ariAge = getSuggestedAge(readabilityScore("ARI", text));
        double fkAge = getSuggestedAge(readabilityScore("FK", text));
        double smogAge = getSuggestedAge(readabilityScore("SMOG", text));
        double clAge = getSuggestedAge(readabilityScore("CL", text));

        return (ariAge + fkAge + smogAge + clAge) / 4;

    }

    public static String readabilityMethod(String choice) {
        return switch (choice) {
            case "ARI" -> "Automated Readability Index";
            case "FK" -> "Flesch-Kincaid readability tests";
            case "SMOG" -> "Simple Measure of Gobbledygook";
            case "CL" -> "Coleman-Liau index";
            default -> throw new IllegalArgumentException(String.format("Option %s not found!", choice));
        };
    }

    public static double readabilityScore(String method, String text)
            throws ArithmeticException {

        return switch (method) {
            case "ARI" -> calculateAriScore(text);
            case "FK" -> calculateFkScore(text);
            case "SMOG" -> calculateSmogScore(text);
            case "CL" -> calculateClScore(text);
            default -> throw new IllegalStateException(String.format("Invalid option: %s", method));
        };
    }

    private static double calculateAriScore(
            int numChars,
            int numWords,
            int numSents
    ) throws ArithmeticException {
        return 4.71 * numChars / numWords +
                0.5 * numWords / numSents - 21.43;

    }

    private static double calculateAriScore(String text) throws ArithmeticException {
        int numChars = countNumberOfChars(text);
        int numWords = countNumberOfWords(text);
        int numSents = countNumberOfSents(text);

        return calculateAriScore(numChars, numWords, numSents);

    }

    private static double calculateFkScore(
            int numWords,
            int numSents,
            int numSyllables
    ) throws ArithmeticException {

        return 0.39 * numWords / numSents +
                11.8 * numSyllables / numWords - 15.59;
    }

    private static double calculateFkScore(String text) throws ArithmeticException {
        int numWords = countNumberOfWords(text);
        int numSents = countNumberOfSents(text);
        int numSyllables = countNumberOfSyllables(text);

        return calculateFkScore(numWords, numSents, numSyllables);

    }

    private static double calculateSmogScore(
            int numPolysyllables,
            int numSents
    ) throws ArithmeticException {
        return 1.043 * Math.sqrt(numPolysyllables * (30.0 / numSents)) + 3.1291;
    }

    private static double calculateSmogScore(String text) throws ArithmeticException {
        int numPolysyllables = countNumberOfPolysyllables(text);
        int numSents = countNumberOfSents(text);

        return calculateSmogScore(numPolysyllables, numSents);
    }

    private static double calculateClScore(int numChars, int numWords, int numSents) {
        double l = (double) numChars / numWords * 100;
        double s = (double) numSents / numWords * 100;

        return 0.0588 * l - 0.296 * s - 15.8;
    }

    private static double calculateClScore(String text) throws ArithmeticException {
        int numChars = countNumberOfChars(text);
        int numWords = countNumberOfWords(text);
        int numSents = countNumberOfSents(text);

        return calculateClScore(numChars, numWords, numSents);

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

    private static int countNumberOfSents(String text) {
        return text.split("[.!?]").length;
    }

    private static int countNumberOfWords(String text) {
        return text.split("\\s+").length;
    }

    private static int countNumberOfChars(String text) {
        return text.replaceAll("\\s", "").length();
    }

    private static int countNumberOfSyllables(String text) {
        int numSyllables = 0;
        String[] words = text.split("\\s");
        for (String word : words) {
            numSyllables += countVowels(word);
        }
        return numSyllables;
    }

    private static int countNumberOfPolysyllables(String text) {
        int numPolysyllables = 0;
        int numVowels;
        String[] words = text.split("\\s");
        for (String word : words) {
            numVowels = countVowels(word);
            numPolysyllables += numVowels >= 3 ? 1 : 0;
        }

        return numPolysyllables;
    }

    private static int countVowels(String word) {
        // initialise number of vowels
        int vowelCount = 0;

        // remove punctuation and special characters
        word = word.replaceAll("[^a-zA-Z0-9]", "");

        // strip last 'e' from word
        word = word.endsWith("e") ? word.substring(0, word.length() - 1) : word;

        // replace consecutive vowels with a single vowel (arbitrarily choose 'a')
        word = word.replaceAll("[AEIOUYaeiouy]{2,}", "a");

        // split into character array
        int[] letters = word.chars().toArray();

        char character;

        for (int letter : letters) {
            character = (char) letter;
            vowelCount += switch (character) {
                case 'A', 'a', 'E', 'e', 'I', 'i', 'O', 'o', 'U', 'u', 'Y', 'y' -> 1;
                default -> 0;
            };
        }

        return vowelCount == 0 ? 1 : vowelCount;

    }


    public static int getSuggestedAge(double score) {
        int roundedScore = (int) Math.ceil(score);

        return roundedScore + 5;


        // return switch (roundedScore) {
        //     case 1 -> 6;
        //     case 2 -> 7;
        //     case 3 -> 8;
        //     case 4 -> 9;
        //     case 5 -> 10;
        //     case 6 -> 11;
        //     case 7 -> 12;
        //     case 8 -> 13;
        //     case 9 -> 14;
        //     case 10 -> 15;
        //     case 11 -> 16;
        //     case 12 -> 17;
        //     case 13 -> 18;
        //     case 14 -> 22;
        //     default -> throw new UnsupportedOperationException(
        //             String.format("Score %.2f not covered by ARI index!", score));
        // };
    }
}
