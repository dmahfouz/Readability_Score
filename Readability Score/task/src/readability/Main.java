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

        printAriSummary(text);

    }

    // stage 3 methods

    public static void printAriSummary(String text) {

        int numWords = countNumberOfWords(text);
        int numSents = countNumberOfSentences(text);
        int numChars = countNumberOfCharacters(text);
        double ariScore = calculateARI(text);
        String suggestedAge = getSuggestedAgeRange(ariScore);

        System.out.println("The text is:");
        System.out.println(text);

        System.out.printf("Words: %d%n", numWords);
        System.out.printf("Sentences: %d%n", numSents);
        System.out.printf("Characters: %d%n", numChars);
        System.out.printf("The score is: %.2f%n", ariScore);

        System.out.println(suggestedAge.equals("N/A") ?
                "Score does not exist in ARI!" :
                String.format("This text should be understood by %s year-olds", suggestedAge));

    }

    public static String readInput() {
        // Read text from user input
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static String readFile(String filePath) {
        try {
            return readFileAsString(filePath);
        } catch (IOException e) {
            System.out.printf("Error: file %s does not exist!\n", filePath);
            throw new RuntimeException(e);
        }
    }

    private static String readFileAsString(String filePath) throws IOException {
        // Read entire file as String
        Path fp = Paths.get(filePath);
        return new String(Files.readAllBytes(fp));
    }


    public static String getSuggestedAgeRange(double ari) {
        // Returns suggested age bracket based on the Automated readability
        // index (ARI) score
        int roundedARI = (int) Math.ceil(ari);

        String suggestedAgeRange = switch(roundedARI) {
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

        return suggestedAgeRange;


    }

    public static double calculateARI(String text) {
        // Calculates Automated readability index (ARI)

        double numChars = countNumberOfCharacters(text);
        double numWords = countNumberOfWords(text);
        double numSents = countNumberOfSentences(text);

        return 4.71 * (numChars / numWords) +
                0.5 * (numWords / numSents) - 21.43;
    }

    // stage 2 methods

    public static void estimateDifficultyUsingWords(String text) {
        int avgWordCount = calcAvgWordsPerSentence(text);
        printDifficulty(avgWordCount, 10);
    }

    public static void estimateDifficultyUsingSymbols(String text) {
        int charCount = countNumberOfCharacters(text);
        printDifficulty(charCount, 100);
    }

    public static void printDifficulty(int count, int threshold) {
        System.out.println(count <= threshold ? "EASY": "HARD");
    }

    private static int calcAvgWordsPerSentence(String text) {
        int numWords = countNumberOfWords(text);
        int numSentences = countNumberOfSentences(text);
        return numWords / numSentences;
    }

    private static int countNumberOfSentences(String text) {
        return text.split("[?!.]").length;
    }

    private static int countNumberOfWords(String text) {
        return text.split("\\s+").length;
    }

    private static int countNumberOfCharacters(String text) {
        int[] chars = textToChars(text, "\s");
        return chars.length;
    }

    private static int[] textToChars(String text, String regexExclude) {
        return textToChars(text.replaceAll(regexExclude, ""));
    }

    private static int[] textToChars(String text) {
        return text.chars().toArray();
    }


}
